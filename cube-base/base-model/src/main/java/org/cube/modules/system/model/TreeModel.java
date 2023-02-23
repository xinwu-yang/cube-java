package org.cube.modules.system.model;

import lombok.Data;
import org.cube.modules.system.entity.SysPermission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形列表用到
 */
@Data
public class TreeModel implements Serializable {

    private String key;

    private String title;

    private String slotTitle;

    private boolean isLeaf;

    private String icon;

    private Integer ruleFlag;

    private Map<String, String> scopedSlots;

    private String parentId;

    private String label;

    private String value;

    private List<TreeModel> children;

    public TreeModel(SysPermission permission) {
        this.key = permission.getId();
        this.icon = permission.getIcon();
        this.parentId = permission.getParentId();
        this.title = permission.getName();
        this.slotTitle = permission.getName();
        this.value = permission.getId();
        this.isLeaf = permission.isLeaf();
        this.label = permission.getName();
        if (!permission.isLeaf()) {
            this.children = new ArrayList<>();
        }
    }

    public TreeModel(String key, String parentId, String slotTitle, Integer ruleFlag, boolean isLeaf) {
        this.key = key;
        this.parentId = parentId;
        this.ruleFlag = ruleFlag;
        this.slotTitle = slotTitle;
        Map<String, String> map = new HashMap<>();
        map.put("title", "hasDatarule");
        this.scopedSlots = map;
        this.isLeaf = isLeaf;
        this.value = key;
        if (!isLeaf) {
            this.children = new ArrayList<>();
        }
    }
}
