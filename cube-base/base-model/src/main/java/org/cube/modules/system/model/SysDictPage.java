package org.cube.modules.system.model;

import org.cube.modules.system.entity.SysDictItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页字典
 */
@Data
@NoArgsConstructor
public class SysDictPage implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 删除状态
     */
    private Integer delFlag;

    /**
     * 描述
     */
    private String description;

    /**
     * 字典列表
     */
    private List<SysDictItem> sysDictItemList;
}
