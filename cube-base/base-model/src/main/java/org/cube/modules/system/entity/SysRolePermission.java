package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限表
 *
 * @author xinwuy
 * @since 2021-09-27
 */
@Data
@NoArgsConstructor
public class SysRolePermission implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;

    /**
     * 数据权限
     */
    private String dataRuleIds;

    /**
     * 操作时间
     */
    private Date operateDate;

    /**
     * 操作ip
     */
    private String operateIp;

    public SysRolePermission(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
