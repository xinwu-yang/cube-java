package org.cube.codegen.core.models;

import lombok.Data;

/**
 * 表单字段属性
 */
@Data
public class TableField {

    /**
     * 标题
     */
    private String title;

    /**
     * 绑定数据
     */
    private String dataIndex;

    /**
     * 排序
     */
    private int sort;

    /**
     * 分组
     */
    private int groupId;

    /**
     * 是否在列表展示
     */
    private boolean showInList;

    /**
     * 是否在表单中
     */
    private boolean showInForm;

    /**
     * 是否必填
     */
    private boolean require;

    /**
     * 绑定的组件
     */
    private Component component;
}
