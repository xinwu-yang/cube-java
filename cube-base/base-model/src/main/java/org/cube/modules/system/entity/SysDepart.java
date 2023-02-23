package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门表
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-01-22
 */
@Data
public class SysDepart implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 父机构ID
     */
    private String parentId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 英文名
     */
    private String departNameEn;

    /**
     * 缩写
     */
    private String departNameAbbr;

    /**
     * 排序
     */
    private Integer departOrder;

    /**
     * 描述
     */
    private String description;

    /**
     * 机构类别 1组织机构，2岗位
     */
    private String orgCategory;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 传真
     */
    private String fax;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注
     */
    private String memo;

    /**
     * 状态（1启用，0不启用）
     */
    @Dict("depart_status")
    private String status;

    /**
     * 删除状态（0正常、1已删除）
     */
    @Dict("del_flag")
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateTime;

    /**
     * 所属地区
     */
    private Long areaId;

    /**
     * 国标编码
     */
    private String gbCode;
}
