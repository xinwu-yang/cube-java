package org.cube.codegen.core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cube.codegen.annotations.models.ComponentType;

import java.util.Map;

/**
 * 组件
 */
@Data
@NoArgsConstructor
public class Component {

    /**
     * 组件名称
     */
    private ComponentType name;

    /**
     * 基础数据类型
     */
    private String basicType;

    /**
     * 组件参数
     */
    // 组件参数
    private Map<String, String> params;
}
