package org.cube.codegen.core.models;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 表单参数
 */
@Data
public class Table implements Serializable {
    public static final TableGroup DEFAULT_GROUP = new TableGroup(0, "默认分组");

    /**
     * Java代码生成相关参数
     */
    private JavaCodeParams javaCodeParams;

    /**
     * 是否为分组表单
     */
    private boolean grouped;

    /**
     * 字段列表
     */
    private List<TableField> fieldList;

    /**
     * 查询条件列表
     */
    private List<TableQueryField> tableQueryFieldList = ListUtil.toList();

    /**
     * 分组列表
     */
    private List<TableGroup> tableGroupList = ListUtil.toList(DEFAULT_GROUP);

    /**
     * 子表
     */
    private List<SubTable> subTableList = ListUtil.toList();

    /**
     * 拓展参数
     */
    private Map<String, Object> extra;
}
