package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.entity.SysDepartPermission;
import org.cube.modules.system.entity.SysPermissionDataRule;
import org.cube.modules.system.model.api.response.LoadDataRuleResponse;
import org.cube.modules.system.model.api.response.TreeListForDeptRoleResponse;

import java.util.List;

/**
 * 部门权限表
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */

public interface ISysDepartPermissionService extends IService<SysDepartPermission> {

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     */
    void saveDepartPermission(String departId, String permissionIds, String lastPermissionIds);

    /**
     * 根据部门id，菜单id获取数据规则
     */
    List<SysPermissionDataRule> getPermRuleListByDeptIdAndPermId(String departId, String permissionId);

    /**
     * 获取部门下指定菜单的数据权限
     */
    LoadDataRuleResponse loadDataRule(String permissionId, String departId);

    /**
     * 保存数据权限关系
     *
     * @param permissionId 菜单id
     * @param departId     部门id
     * @param dataRuleIds  数据权限
     */
    void saveDataRule(String permissionId, String departId, String dataRuleIds);

    /**
     * 查询部门用户角色授权功能
     *
     * @param departId 部门id
     */
    TreeListForDeptRoleResponse queryTreeListForDeptRole(String departId);
}
