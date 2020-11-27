package org.benrush.baseline.pojo;

import lombok.Data;

@Data
public class PoItem {
    //java数据类型
    private String javaType;
    //字段名称,已经转为驼峰
    private String propertyName;
    //注释
    private String comment;
}
