package org.benrush.server.common.orm;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BasePoHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter("createTime")) {
            if (metaObject.getValue("createTime") == null) {
                setFieldValByName("createTime", LocalDateTime.now(), metaObject);
            }
        }
        if (metaObject.hasGetter("deleted")) {
            if (metaObject.getValue("deleted") == null) {
                setFieldValByName("deleted", false, metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("updateTime")) {
            if (metaObject.getValue("updateTime") == null) {
                setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
            }
        }
    }
}
