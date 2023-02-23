package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cube.modules.system.entity.SysDepartRole;

import java.util.List;

/**
 * 部门角色
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysDepartRoleMapper extends BaseMapper<SysDepartRole> {

    /**
     * 根据用户id，部门id查询可授权所有部门角色
     */
    List<SysDepartRole> queryDeptRoleByDeptAndUser(@Param("orgCode") String orgCode, @Param("userId") String userId);
}
