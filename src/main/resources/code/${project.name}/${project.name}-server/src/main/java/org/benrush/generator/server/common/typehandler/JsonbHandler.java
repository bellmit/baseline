package org.benrush.server.common.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class JsonbHandler extends JsonHandler {
    private static String pgJdbcType = "jsonb";

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType(pgJdbcType);
        jsonObject.setValue(s);
        preparedStatement.setObject(i, jsonObject);
    }
}
