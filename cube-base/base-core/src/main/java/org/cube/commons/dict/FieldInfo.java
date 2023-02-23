package org.cube.commons.dict;

import lombok.Data;

@Data
public class FieldInfo {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字典code
     */
    private String code;

    /**
     * 字典text
     */
    private String text;

    /**
     * 字典表
     */
    private String table;
}
