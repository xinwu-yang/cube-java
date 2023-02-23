package org.cube.codegen.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 查询条件
 */
@Data
@AllArgsConstructor
public class TableQueryField {

    /**
     * 条件参数名称
     */
    private String name;

    /**
     * 条件语义名称
     */
    private String label;

    /**
     * 条件对应组件
     */
    private Component component;

    /**
     * 是否使用字段的组件，name必须和dataIndex一致
     */
    private boolean useFieldComponent;
}
