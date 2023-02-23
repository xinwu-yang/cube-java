package org.cube.codegen.core.models;

import lombok.Data;

/**
 * Java模版生成参数
 */
@Data
public class JavaCodeParams {

    /**
     * 基础包名
     */
    private String businessPackage;

    /**
     * 业务包名
     */
    private String modulePackage;

    /**
     * 实体包名
     */
    private String entityPackage;

    /**
     * 描述
     */
    private String description;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 实体名首字母小写
     */
    private String entityNameLower;
}
