package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门角色权限
 *
 * @author xinwuy
 * @version V1.0
 * @since 2020-02-12
 */
@Data
public class SysDepartRolePermission implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 部门id
     */
    private String departId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;

    /**
     * 数据规则id（多个逗号分隔）
     */
    private String dataRuleIds;

    /**
     * 操作时间
     */
    private Date operateDate;

    /**
     * 操作IP
     */
    private String operateIp;

    public SysDepartRolePermission(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
