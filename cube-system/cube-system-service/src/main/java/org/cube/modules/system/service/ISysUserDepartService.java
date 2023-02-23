package org.cube.modules.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.model.DepartIdModel;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.entity.SysUserDepart;

import java.util.List;

/**
 * 用户部门关系
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {

    /**
     * 根据指定用户id查询部门信息
     */
    List<DepartIdModel> queryDepartIdsOfUser(String userId);

    /**
     * 根据部门id查询用户信息
     */
    List<SysUser> queryUserByDepId(String depId);

    /**
     * 根据部门code，查询当前部门和下级部门的用户信息
     */
    List<SysUser> queryUserByDepCode(String depCode, String realname);
}
