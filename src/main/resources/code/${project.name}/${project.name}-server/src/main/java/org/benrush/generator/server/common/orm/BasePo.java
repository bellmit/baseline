package org.benrush.server.common.orm;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 表基础字段，可根据需要进行自定义的配置
 * @author: 刘希晨
 * @date: 2020/11/3 17:00
 */
@Data
public class BasePo {
    @TableId(type = IdType.ASSIGN_UUID)
    @TableField(fill = FieldFill.INSERT)
    protected String id;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    protected Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime modifyTime;
}
