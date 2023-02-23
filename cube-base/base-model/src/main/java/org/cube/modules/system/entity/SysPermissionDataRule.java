package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单权限规则表
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2021-09-27
 */
@Data
public class SysPermissionDataRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
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
     * 状态值 1有效 0无效
     */
    private String status;

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
