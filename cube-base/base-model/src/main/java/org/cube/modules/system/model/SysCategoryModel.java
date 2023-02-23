package org.cube.modules.system.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分类字典DTO
 */
@Data
@NoArgsConstructor
public class SysCategoryModel implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 父级节点
     */
    private String pid;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型编码
     */
    private String code;
}
