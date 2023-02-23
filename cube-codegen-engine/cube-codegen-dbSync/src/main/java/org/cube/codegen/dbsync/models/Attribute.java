package org.cube.codegen.dbsync.models;

import org.cube.codegen.annotations.models.DefaultValueType;
import lombok.Data;

@Data
public class Attribute {
    // 字段名称
    private String name;
    // 字段Java类型
    private String type;
    // 重写类型
    private String overrideType;
    // 是否允许为null
    private boolean allowNullValue = true;
    // 字段长度
    private int length;
    // 注释
    private String comment;
    // 默认值数据类型
    private DefaultValueType defaultValueType = DefaultValueType.NONE;
    // 默认值
    private String defaultValue;
}
