package org.cube.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cube.commons.base.CubeController;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysDepartPermission;
import org.cube.modules.system.entity.SysDepartRolePermission;
import org.cube.modules.system.model.api.request.SaveDataRuleRequest;
import org.cube.modules.system.model.api.request.SaveDeptRolePermissionRequest;
import org.cube.modules.system.model.api.response.LoadDataRuleResponse;
import org.cube.modules.system.model.api.response.TreeListForDeptRoleResponse;
import org.cube.modules.system.service.ISysDepartPermissionService;
import org.cube.modules.system.service.ISysDepartRolePermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门权限
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-02-11
 */
@Slf4j
@Tag(name = "部门权限相关接口")
@RestController
@RequestMapping("/sys/depart/permission")
public class SysDepartPermissionController extends CubeController<SysDepartPermission, ISysDepartPermissionService> {

    @Autowired
    private ISysDepartRolePermissionService sysDepartRolePermissionService;

    /**
     * 查询指定部门是否有数据规则
     *
     * @param permissionId 权限id
     * @param departId     部门id
     */
    @GetMapping("/dataRule/{permissionId}/{departId}")
    public Result<LoadDataRuleResponse> loadDataRule(@PathVariable String permissionId, @PathVariable String departId) {
        LoadDataRuleResponse response = service.loadDataRule(permissionId, departId);
        return Result.ok(response);
    }

    /**
     * 保存数据规则
     *
     * @param request 参数
     * @apiNote 保存数据规则至部门菜单关联表
     */
    @PostMapping("/dataRule")
    public Result<?> saveDataRule(@Validated @RequestBody SaveDataRuleRequest request) {
        service.saveDataRule(request.getPermissionId(), request.getDepartId(), request.getDataRuleIds());
        return Result.ok();
    }

    /**
     * 查询角色授权
     *
     * @param roleId 角色id
     */
    @GetMapping("/queryDeptRolePermission")
    public Result<List<String>> queryDeptRolePermission(@RequestParam String roleId) {
        List<SysDepartRolePermission> list = sysDepartRolePermissionService.list(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getRoleId, roleId));
        List<String> permissions = list.stream().map(SysDepartRolePermission -> String.valueOf(SysDepartRolePermission.getPermissionId())).collect(Collectors.toList());
        return Result.ok(permissions);
    }

    /**
     * 保存角色授权
     *
     * @param request 参数
     */
    @PostMapping("/saveDeptRolePermission")
    public Result<?> saveDeptRolePermission(@RequestBody SaveDeptRolePermissionRequest request) {
        sysDepartRolePermissionService.saveDeptRolePermission(request.getRoleId(), request.getPermissionIds(), request.getLastpermissionIds());
        return Result.ok();
    }

    /**
     * 查询部门用户角色授权功能
     *
     * @param departId 部门id
     * @apiNote 查询菜单权限树
     */
    @GetMapping("/queryTreeListForDeptRole")
    public Result<TreeListForDeptRoleResponse> queryTreeListForDeptRole(@RequestParam String departId) {
        TreeListForDeptRoleResponse response = service.queryTreeListForDeptRole(departId);
        return Result.ok(response);
    }
}
