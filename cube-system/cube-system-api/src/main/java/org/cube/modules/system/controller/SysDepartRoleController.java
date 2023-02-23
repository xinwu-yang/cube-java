package org.cube.modules.system.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import org.cube.commons.base.CubeController;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysPermissionDataRule;
import org.cube.modules.system.service.ISysDepartPermissionService;
import org.cube.modules.system.service.ISysDepartRolePermissionService;
import org.cube.modules.system.service.ISysDepartRoleService;
import org.cube.modules.system.service.ISysDepartRoleUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.modules.system.entity.SysDepartRole;
import org.cube.modules.system.entity.SysDepartRolePermission;
import org.cube.modules.system.entity.SysDepartRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门角色
 *
 * @author xinwuy
 * @version V2.3.5
 * @since 2021-12-21
 */
@Slf4j
@Tag(name = "部门角色相关接口")
@RestController
@RequestMapping("/sys/depart/role")
public class SysDepartRoleController extends CubeController<SysDepartRole, ISysDepartRoleService> {

    @Autowired
    private ISysDepartRoleUserService departRoleUserService;
    @Autowired
    private ISysDepartPermissionService sysDepartPermissionService;
    @Autowired
    private ISysDepartRolePermissionService sysDepartRolePermissionService;

    /**
     * 分页列表查询
     *
     * @param sysDepartRole 查询参数
     * @param pageNo        页码
     * @param pageSize      每页条数
     * @param deptId        部门id
     */
    @DictMethod
    @GetMapping("/list")
    public Result<IPage<SysDepartRole>> queryPageList(SysDepartRole sysDepartRole, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(required = false) String deptId, HttpServletRequest req) {
        QueryWrapper<SysDepartRole> queryWrapper = QueryGenerator.initQueryWrapper(sysDepartRole, req.getParameterMap());
        Page<SysDepartRole> page = new Page<>(pageNo, pageSize);
        //我的部门，选中部门只能看当前部门下的角色
        queryWrapper.eq("depart_id", deptId);
        IPage<SysDepartRole> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     *
     * @param sysDepartRole 角色信息
     */
    @AutoLog("部门角色-添加")
    @PostMapping("/add")
    public Result<?> add(@RequestBody SysDepartRole sysDepartRole) {
        service.save(sysDepartRole);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param sysDepartRole 角色信息
     */
    @AutoLog("部门角色-编辑")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysDepartRole sysDepartRole) {
        service.updateById(sysDepartRole);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 角色id
     */
    @AutoLog("部门角色-通过id删除")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        return service.delete(ListUtil.of(id));
    }

    /**
     * 批量删除
     *
     * @param ids 部门权限id（逗号分隔）
     */
    @AutoLog("部门角色-批量删除")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        return service.delete(Arrays.asList(ids.split(",")));
    }

    /**
     * 获取用户所属部门角色列表
     *
     * @param departId 部门id
     */
    @GetMapping("/getDeptRoleList")
    public Result<List<SysDepartRole>> getDeptRoleList(@RequestParam String departId) {
        LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
        query.eq(SysDepartRole::getDepartId, departId);
        List<SysDepartRole> deptRoleList = service.list(query);
        return Result.ok(deptRoleList);
    }

    /**
     * 为用户设置角色
     *
     * @param json 参数
     */
    @PostMapping("/deptRoleUserAdd")
    public Result<?> deptRoleAdd(@RequestBody JsonNode json) {
        String newRoleId = json.get("newRoleId").asText();
        String oldRoleId = json.get("oldRoleId").asText();
        String userId = json.get("userId").asText();
        departRoleUserService.deptRoleUserAdd(userId, newRoleId, oldRoleId);
        return Result.ok();
    }

    /**
     * 根据用户id获取已设置部门角色
     *
     * @param userId   用户id
     * @param departId 部门id
     */
    @GetMapping("/getDeptRoleByUserId")
    public Result<List<SysDepartRoleUser>> getDeptRoleByUserId(@RequestParam String userId, @RequestParam String departId) {
        //查询部门下角色
        List<SysDepartRole> roleList = service.list(new QueryWrapper<SysDepartRole>().eq("depart_id", departId));
        List<String> roleIds = roleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
        //根据角色id,用户id查询已授权角色
        List<SysDepartRoleUser> roleUserList = departRoleUserService.list(new QueryWrapper<SysDepartRoleUser>().eq("user_id", userId).in("drole_id", roleIds));
        return Result.ok(roleUserList);
    }

    /**
     * 查询数据规则数据
     *
     * @param permissionId 权限id
     * @param departId     部门id
     * @param roleId       角色id
     */
    @GetMapping("/dataRule/{permissionId}/{departId}/{roleId}")
    public Result<?> loadDataRule(@PathVariable String permissionId, @PathVariable String departId, @PathVariable String roleId) {
        //查询已授权的部门规则
        List<SysPermissionDataRule> list = sysDepartPermissionService.getPermRuleListByDeptIdAndPermId(departId, permissionId);
        if (list == null || list.size() == 0) {
            return Result.error("未找到权限配置信息！");
        }
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("datarule", list);
        LambdaQueryWrapper<SysDepartRolePermission> query = new LambdaQueryWrapper<SysDepartRolePermission>().eq(SysDepartRolePermission::getPermissionId, permissionId).eq(SysDepartRolePermission::getRoleId, roleId);
        SysDepartRolePermission sysRolePermission = sysDepartRolePermissionService.getOne(query);
        if (sysRolePermission == null) {
            return Result.error("未找到角色菜单配置信息！");
        }
        String drChecked = sysRolePermission.getDataRuleIds();
        if (StrUtil.isNotEmpty(drChecked)) {
            returnData.put("drChecked", drChecked.endsWith(",") ? drChecked.substring(0, drChecked.length() - 1) : drChecked);
        }
        return Result.ok(returnData);
    }

    /**
     * 保存数据规则至角色菜单关联表
     *
     * @param jsonObject 参数
     */
    @PostMapping("/dataRule")
    public Result<?> saveDataRule(@RequestBody JsonNode jsonObject) {
        String permissionId = jsonObject.get("permissionId").asText();
        String roleId = jsonObject.get("roleId").asText();
        String dataRuleIds = jsonObject.get("dataRuleIds").asText();
        LambdaQueryWrapper<SysDepartRolePermission> query = new LambdaQueryWrapper<SysDepartRolePermission>().eq(SysDepartRolePermission::getPermissionId, permissionId).eq(SysDepartRolePermission::getRoleId, roleId);
        SysDepartRolePermission sysRolePermission = sysDepartRolePermissionService.getOne(query);
        if (sysRolePermission == null) {
            return Result.error("请先保存角色菜单权限！");
        }
        sysRolePermission.setDataRuleIds(dataRuleIds);
        sysDepartRolePermissionService.updateById(sysRolePermission);
        return Result.ok();
    }
}
