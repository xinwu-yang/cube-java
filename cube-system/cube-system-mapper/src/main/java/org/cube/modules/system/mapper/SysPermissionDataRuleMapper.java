package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cube.modules.system.entity.SysPermissionDataRule;

import java.util.List;

/**
 * 权限规则
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysPermissionDataRuleMapper extends BaseMapper<SysPermissionDataRule> {

    /**
     * 根据用户名和权限id查询
     */
    List<String> queryDataRuleIds(@Param("username") String username, @Param("permissionId") String permissionId);
}
