package org.cube.modules.system.model.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SaveDeptRolePermissionRequest {

    /**
     * 角色id
     */
    @NotBlank
    private String roleId;

    /**
     * 菜单id
     */
    private String permissionIds;

    /**
     * 上次的菜单id
     */
    private String lastpermissionIds;
}
