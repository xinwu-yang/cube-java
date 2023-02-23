package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.entity.SysDepartRolePermission;

/**
 * 部门角色权限
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysDepartRolePermissionService extends IService<SysDepartRolePermission> {

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     */
    void saveDeptRolePermission(String roleId, String permissionIds, String lastPermissionIds);
}
