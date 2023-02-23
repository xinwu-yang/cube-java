package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.entity.SysUserRole;

import java.util.List;

/**
 * 用户角色
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    List<String> getRoleByUserName(String username);

    List<String> getRoleIdByUserName(String username);
}
