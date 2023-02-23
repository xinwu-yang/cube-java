package org.cube.codegen.core.models;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import org.cube.codegen.annotations.models.RelationType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 子表代码相关参数
 */
@Data
public class SubTable implements Serializable {

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 实体名首字母小写
     */
    private String entityNameLower;

    /**
     * 实体包名
     */
    private String entityPackage;

    /**
     * 描述
     */
    private String description;

    /**
     * 外键
     */
    private String foreignKey;

    /**
     * 关系类型
     */
    private RelationType relationType;

    /**
     * 字段列表
     */
    private List<TableField> fieldList;

    /**
     * 查询条件列表
     */
    private List<TableQueryField> tableQueryFieldList = ListUtil.toList();

    /**
     * 拓展参数
     */
    private Map<String, Object> extra;
}
