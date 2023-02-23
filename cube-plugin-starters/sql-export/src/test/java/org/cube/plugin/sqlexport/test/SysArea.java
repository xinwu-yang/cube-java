package org.cube.plugin.sqlexport.test;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 地区管理
 */
@TableName("sys_area")
public class SysArea implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 父Id
     */
    @TableField(exist = false)
    private Long parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 国标编码
     */
    private String gbCode;
    /**
     * 邮编
     */
    private String postCode;
    /**
     * 菜单等级
     */
    private Integer level;
    /**
     * 全路径
     */
    private String path;
    /**
     * 是否有子节点
     */
    private Boolean hasChildren;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @TableLogic
    private Integer delFlag;
    /**
     * 创建日期
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGbCode() {
        return gbCode;
    }

    public void setGbCode(String gbCode) {
        this.gbCode = gbCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SysArea() {
    }
}
