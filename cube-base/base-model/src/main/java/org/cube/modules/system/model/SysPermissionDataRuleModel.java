package org.cube.modules.system.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 菜单权限规则表
 *
 * @author huangzhilin
 * @since 2019-03-29
 */
@Data
@NoArgsConstructor
public class SysPermissionDataRuleModel {

    /**
     * 主键
     */
    private String id;

    /**
     * 对应的菜单id
     */
    private String permissionId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 字段
     */
    private String ruleColumn;

    /**
     * 条件
     */
    private String ruleConditions;

    /**
     * 规则值
     */
    private String ruleValue;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;
}
