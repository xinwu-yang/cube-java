package org.cube.modules.system.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.base.Result;
import org.cube.modules.system.service.ISysPermissionDataRuleService;
import org.cube.modules.system.service.ISysPermissionService;
import org.cube.modules.system.service.ISysRolePermissionService;
import org.cube.modules.system.service.ISysRoleService;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.SysPermission;
import org.cube.modules.system.entity.SysPermissionDataRule;
import org.cube.modules.system.entity.SysRole;
import org.cube.modules.system.entity.SysRolePermission;
import org.cube.modules.system.model.TreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 角色
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2021-10-08
 */
@Slf4j
@Tag(name = "角色相关接口")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends CubeController<SysRole, ISysRoleService> {

    @Autowired
    private ISysPermissionDataRuleService sysPermissionDataRuleService;
    @Autowired
    private ISysRolePermissionService sysRolePermissionService;
    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 分页列表查询
     *
     * @param role     查询参数
     * @param pageNo   页码
     * @param pageSize 每页大小
     */
    @GetMapping("/list")
    public Result<IPage<SysRole>> queryPageList(SysRole role, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(role, req.getParameterMap());
        Page<SysRole> page = new Page<>(pageNo, pageSize);
        IPage<SysRole> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     *
     * @param role 角色信息
     */
    @AutoLog("角色管理-添加角色")
    @PostMapping("/add")
    public Result<SysRole> add(@RequestBody SysRole role) {
        service.save(role);
        return Result.ok();
    }

    /**
     * 更新
     *
     * @param role 角色信息
     */
    @AutoLog("角色管理-编辑角色")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysRole role) {
        SysRole sysrole = service.getById(role.getId());
        if (sysrole == null) {
            return Result.error("未找到对应实体！");
        }
        service.updateById(role);
        return Result.ok();
    }

    /**
     * 通过id删除角色
     *
     * @param id 角色id
     */
    @AutoLog("角色管理-通过id删除角色")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        service.deleteRole(id);
        return Result.ok();
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色id（多个逗号分隔）
     */
    @AutoLog("角色管理-批量删除角色")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (StrUtil.isEmpty(ids)) {
            return Result.error("未选中角色！");
        }
        service.deleteBatchRole(ids.split(","));
        return Result.ok();
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/queryAll")
    public Result<List<SysRole>> queryAll() {
        List<SysRole> list = service.list();
        if (list == null || list.size() <= 0) {
            return Result.error("未找到角色信息！");
        }
        return Result.ok(list);
    }

    /**
     * 校验角色编码唯一
     *
     * @param id       角色id
     * @param roleCode 角色编码
     */
    @GetMapping("/checkRoleCode")
    public Result<?> checkUsername(String id, String roleCode) {
        SysRole role = null;
        if (StrUtil.isNotEmpty(id)) {
            role = service.getById(id);
        }
        SysRole newRole = service.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, roleCode));
        if (newRole != null) {
            //如果根据传入的roleCode查询到信息了，那么就需要做校验了。
            if (role == null || !id.equals(newRole.getId())) {
                //role为空=>新增模式=>只要roleCode存在则返回false
                //否则=>编辑模式=>判断两者ID是否一致
                return Result.error("角色编码已存在！");
            }
        }
        return Result.ok();
    }

    /**
     * 导出角色数据为Excel
     *
     * @param sysRole 查询条件
     */
    @GetMapping("/exportXls")
    public void exportXls(SysRole sysRole, HttpServletRequest request, HttpServletResponse response) throws IOException {
        QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(sysRole, request.getParameterMap());
        List<SysRole> pageList = service.list(queryWrapper);
        HttpServletUtil.addDownloadHeader(response, "角色数据-" + DateUtil.format(new Date(), "yyyyMMdd") + easyExcel.getExtension());
        easyExcel.export(pageList, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * 通过Excel导入角色数据
     */
    @AutoLog("角色管理-通过Excel导入角色数据")
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            ImportExcel excel = new ImportExcel();
            excel.setInputStream(file.getInputStream());
            List<SysRole> sysRoles = easyExcel.read(SysRole.class, excel, SystemContextUtil.dictTranslator());
            service.saveBatch(sysRoles);
        }
        return Result.ok();
    }

    /**
     * 查询数据规则数据
     *
     * @param permissionId 菜单id
     * @param roleId       角色id
     */
    @GetMapping("/dataRule/{permissionId}/{roleId}")
    public Result<?> loadDataRule(@PathVariable String permissionId, @PathVariable String roleId) {
        List<SysPermissionDataRule> list = sysPermissionDataRuleService.getPermRuleListByPermId(permissionId);
        if (list == null || list.size() == 0) {
            return Result.error("未找到权限配置信息");
        }
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("datarule", list);
        LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getPermissionId, permissionId).isNotNull(SysRolePermission::getDataRuleIds).eq(SysRolePermission::getRoleId, roleId);
        SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
        if (sysRolePermission != null) {
            String drChecked = sysRolePermission.getDataRuleIds();
            if (StrUtil.isNotEmpty(drChecked)) {
                returnData.put("drChecked", drChecked.endsWith(",") ? drChecked.substring(0, drChecked.length() - 1) : drChecked);
            }
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
        LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getPermissionId, permissionId).eq(SysRolePermission::getRoleId, roleId);
        SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
        if (sysRolePermission == null) {
            return Result.error("请先保存角色菜单权限！");
        }
        sysRolePermission.setDataRuleIds(dataRuleIds);
        this.sysRolePermissionService.updateById(sysRolePermission);
        return Result.ok();
    }

    /**
     * 用户角色授权功能
     *
     * @apiNote 查询菜单权限树
     */
    @GetMapping("/queryTreeList")
    public Result<?> queryTreeList() {
        //全部权限ids
        List<String> ids = new ArrayList<>();
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysPermission::getSortNo);
        List<SysPermission> list = sysPermissionService.list(query);
        for (SysPermission sysPer : list) {
            ids.add(sysPer.getId());
        }
        List<TreeModel> treeList = new ArrayList<>();
        getTreeModelList(treeList, list, null);
        Map<String, Object> returnData = new HashMap<>();
        //全部树节点数据
        returnData.put("treeList", treeList);
        //全部树ids
        returnData.put("ids", ids);
        return Result.ok(returnData);
    }

    private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(), permission.getRuleFlag(), permission.isLeaf());
            if (temp == null && StrUtil.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.isLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.isLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }
        }
    }
}
