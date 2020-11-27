package org.benrush.baseline.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.common.BaseLineException;
import org.benrush.baseline.common.Constants;
import org.benrush.baseline.database.ColumnInfo;
import org.benrush.baseline.database.DatabaseConnector;
import org.benrush.baseline.database.DatabaseConstants;
import org.benrush.baseline.pojo.PoItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DatabaseUtil {
    private DatabaseUtil(){

    }

    private static final Map<String, List<PoItem>> poColumnInfo = new HashMap<>();

    private static final Map<String,String> poCommentInfo = new HashMap<>();

    private static final String[] tables = ConfigUtil.getConfig(Constants.TABLES).split(",");

    //是否运行sql文件，0表示运行，1表示直接从库里提取
    private static final Integer mode = Integer.valueOf(ConfigUtil.getConfig(Constants.DATABASE_MODE));

    private static final String prefix = ConfigUtil.getConfig(Constants.PREFIX);

    private static final DatabaseConnector connector = new DatabaseConnector();

    static{
        //如果mode为0，运行数据库脚本
        if(mode.equals(0)){
            try {
                connector.runScript();
            } catch (IOException e) {
                log.error("运行数据库脚本出错");
                e.printStackTrace();
            }
        }
        //载入表字段类型信息和注释信息
        for(String table : tables){
            try {
                //获取表字段信息
                Map<String, ColumnInfo> columnInfoMap = connector.queryTableColumns(table);
                //获取表注释信息
                String tableComment = connector.queryTableComment(table);
                List<PoItem> poItemList = new ArrayList<>();
                for(Map.Entry<String,ColumnInfo> entry : columnInfoMap.entrySet()){
                    ColumnInfo columnInfo = entry.getValue();
                    String propertyName = entry.getKey();
                    PoItem poItem = new PoItem();
                    poItem.setComment(columnInfo.getComment());
                    poItem.setJavaType(getJavaType(columnInfo.getUdtName()));
                    //下划线转驼峰
                    poItem.setPropertyName(FormatUtil.underline2Camel(propertyName));
                    poItemList.add(poItem);
                }
                poColumnInfo.put(table,poItemList);
                poCommentInfo.put(table,tableComment);
            } catch (SQLException e) {
                log.info("处理表 " + table + "出错");
                e.printStackTrace();
            }
        }
        connector.close();
    }

    public static String[] getTables(){
        return tables;
    }

    public static String getJavaType(String type){
        if(type.equals(DatabaseConstants.JSON) || type.equals(DatabaseConstants.JSONB) || type.equals(DatabaseConstants.VARCHAR)) {
            return "String";
        }else if(type.equals(DatabaseConstants.TIMESTAMP)){
            return "LocalDateTime";
        }else if(type.equals(DatabaseConstants.INT2)){
            return "Short";
        }else if(type.equals(DatabaseConstants.INT4)){
            return "Integer";
        }else if(type.equals(DatabaseConstants.INT8)){
            return "Long";
        }else if(type.equals(DatabaseConstants.BOOLEAN)){
            return "Boolean";
        }
        throw new BaseLineException("未录入的数据类型，请手动解析");
    }

    public static List<PoItem> getPoColumnInfo(String tableName){
        return poColumnInfo.get(tableName);
    }

    public static String getPoComment(String tableName){
        return poCommentInfo.get(tableName);
    }

    //去掉前缀
    public static String removePrefix(String tableName){
        if(StrUtil.isEmpty(prefix)){
            return tableName;
        }
        String[] s = tableName.split("_");
        if(!prefix.equals(s[0])){
            throw new BaseLineException("错误的数据库表名前缀，请确认表名都具有相同的前缀");
        }
        //去掉前缀和_所占用的长度
        return tableName.substring(prefix.length() + 1);
    }

    //首字母大写
    public static String UpperTheFirstLetter(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
