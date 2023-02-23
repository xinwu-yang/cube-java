package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.entity.SysRole;

/**
 * 角色
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 删除角色
     */
    void deleteRole(String roleId);

    /**
     * 批量删除角色
     */
    void deleteBatchRole(String[] roleIds);
}
