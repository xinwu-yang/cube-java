package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.entity.SysRolePermission;

/**
 * 角色权限
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 保存授权/先删后增
     */
    void saveRolePermission(String roleId, String permissionIds);

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     */
    void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds);
}
