package org.benrush.baseline.database;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.benrush.baseline.util.ConfigUtil;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DatabaseConnector {
    private final String url = ConfigUtil.getConfig("database.url");
    private final String username = ConfigUtil.getConfig("database.username");
    private final String password = ConfigUtil.getConfig("database.password");
    private final String driver = ConfigUtil.getConfig("database.driver");
    private final String schema = ConfigUtil.getConfig("database.schema");

    private final String queryAllTableInfo = "select col_description((table_schema||'.'||table_name)::regclass::oid, ordinal_position) as column_comment" +
            ", * from information_schema.columns " +
            "WHERE table_schema = ? " +
            "and table_name = ?";

    private final String queryTableComment = "SELECT n.nspname as \"Schema\"," +
            "  c.relname as \"Name\"," +
            "  pg_catalog.pg_size_pretty(pg_catalog.pg_table_size(c.oid)) as \"Size\"," +
            "  pg_catalog.obj_description(c.oid, 'pg_class') as \"Description\"" +
            "FROM pg_catalog.pg_class c" +
            "     LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace" +
            "WHERE c.relname = ?";

    private Connection connection;

    public DatabaseConnector() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("连接数据库失败，配置项有误");
            System.exit(0);
        } finally {
            log.info("成功进入数据库");
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("关闭数据库出现问题");
        }
    }

    /**
     * @Description: 获取所有表的信息，表名用逗号分隔开
     * @author: 刘希晨
     * @date: 2020/11/12 15:14
     */
    public Map<String, ColumnInfo> queryTableColumns(String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(queryAllTableInfo);
        preparedStatement.setString(1, schema);
        preparedStatement.setString(2, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, ColumnInfo> result = new HashMap<>();
        while (resultSet.next()) {
            String column = resultSet.getString("column_name");//字段的名称
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setUdtName(resultSet.getString("udt_name"));//字段类型
            columnInfo.setMaxLength(resultSet.getString("character_maximum_length")); //可能为空
            columnInfo.setComment(resultSet.getString("column_comment"));//可能未注释
            result.put(column,columnInfo);
        }
        preparedStatement.close();
        resultSet.close();
        return result;
    }

    /**
     * @Description: 获取表的大小与注释
     * @author: 刘希晨
     * @date: 2020/11/13 9:34
     */
    public String queryTableComment(String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(queryTableComment);
        preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, ColumnInfo> result = new HashMap<>();
        while (resultSet.next()) {
            if (resultSet.getString("Schema").equals(schema)) {
                log.info("导入{}表,大小为{}", resultSet.getString("Name"), resultSet.getString("Size"));
                return resultSet.getString("Description");
            }
        }
        preparedStatement.close();
        resultSet.close();
        throw new RuntimeException("数据库中未找到表" + tableName);
    }

    public void runScript() throws IOException {
        File[] sqls = FileUtil.ls(FileUtil.getAbsolutePath("sql"));
        // 创建ScriptRunner，用于执行SQL脚本
        ScriptRunner runner = new ScriptRunner(connection);
        PrintWriter printWriter = new PrintWriter(System.out);
        runner.setLogWriter(printWriter);
        // 执行sql文件夹下的所有SQL脚本
        for (File sql : sqls) {
            Reader sqlReader = new FileReader(sql);
            log.info("准备执行脚本 " + sql.getName());
            runner.runScript(sqlReader);
            log.info("数据库脚本 " + sql.getName() + " 运行成功");
        }
    }




}
