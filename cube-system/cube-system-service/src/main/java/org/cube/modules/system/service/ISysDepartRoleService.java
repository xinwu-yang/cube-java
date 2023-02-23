package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysDepartRole;

import java.util.List;

/**
 * 部门角色
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysDepartRoleService extends IService<SysDepartRole> {

    /**
     * 根据用户id，部门id查询可授权所有部门角色
     */
    List<SysDepartRole> queryDeptRoleByDeptAndUser(String orgCode, String userId);

    /**
     * 批量删除部门角色
     *
     * @param ids 部门角色id集合
     * @return 结果
     */
    Result<?> delete(List<String> ids);
}
