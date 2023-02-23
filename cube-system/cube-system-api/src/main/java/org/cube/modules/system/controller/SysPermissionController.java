package org.cube.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.base.CubeController;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysDepartPermission;
import org.cube.modules.system.entity.SysPermission;
import org.cube.modules.system.entity.SysPermissionDataRule;
import org.cube.modules.system.entity.SysRolePermission;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.model.SysPermissionTree;
import org.cube.modules.system.model.TreeModel;
import org.cube.modules.system.service.ISysDepartPermissionService;
import org.cube.modules.system.service.ISysPermissionDataRuleService;
import org.cube.modules.system.service.ISysPermissionService;
import org.cube.modules.system.service.ISysRolePermissionService;
import org.cube.commons.utils.PermissionDataUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统菜单
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2021-10-13
 */
@Slf4j
@Tag(name = "系统菜单相关接口")
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController extends CubeController<SysPermission, ISysPermissionService> {

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;
    @Autowired
    private ISysPermissionDataRuleService sysPermissionDataRuleService;
    @Autowired
    private ISysDepartPermissionService sysDepartPermissionService;

    /**
     * 加载菜单节点
     */
    @GetMapping("/list")
    public Result<List<SysPermissionTree>> list() {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysPermission::getSortNo);
        List<SysPermission> list = service.list(query);
        List<SysPermissionTree> treeList = new ArrayList<>();
        getTreeList(treeList, list, null);
        return Result.ok(treeList);
    }

    /**
     * 系统菜单列表
     *
     * @apiNote 仅加载一级菜单
     */
    @GetMapping("/getSystemMenuList")
    public Result<List<SysPermissionTree>> getSystemMenuList() {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getMenuType, CommonConst.MENU_TYPE_0);
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysPermission::getSortNo);
        List<SysPermission> list = service.list(query);
        List<SysPermissionTree> sysPermissionTreeList = new ArrayList<>();
        for (SysPermission sysPermission : list) {
            SysPermissionTree sysPermissionTree = new SysPermissionTree(sysPermission);
            sysPermissionTreeList.add(sysPermissionTree);
        }
        return Result.ok(sysPermissionTreeList);
    }

    /**
     * 查询子菜单
     *
     * @param parentId 父id
     */
    @GetMapping("/getSystemSubmenu")
    public Result<List<SysPermissionTree>> getSystemSubmenu(@RequestParam String parentId) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getParentId, parentId);
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysPermission::getSortNo);
        List<SysPermission> list = service.list(query);
        List<SysPermissionTree> sysPermissionTreeList = new ArrayList<>();
        for (SysPermission sysPermission : list) {
            SysPermissionTree sysPermissionTree = new SysPermissionTree(sysPermission);
            sysPermissionTreeList.add(sysPermissionTree);
        }
        return Result.ok(sysPermissionTreeList);
    }

    /**
     * 批量查询子菜单
     *
     * @param parentIds 父id（多个逗号分隔）
     */
    @GetMapping("/getSystemSubmenuBatch")
    public Result<?> getSystemSubmenuBatch(@RequestParam String parentIds) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        List<String> parentIdList = Arrays.asList(parentIds.split(","));
        query.in(SysPermission::getParentId, parentIdList);
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysPermission::getSortNo);
        List<SysPermission> list = service.list(query);
        Map<String, List<SysPermissionTree>> listMap = new HashMap<>();
        for (SysPermission item : list) {
            String pid = item.getParentId();
            if (parentIdList.contains(pid)) {
                List<SysPermissionTree> mapList = listMap.get(pid);
                if (mapList == null) {
                    mapList = new ArrayList<>();
                }
                mapList.add(new SysPermissionTree(item));
                listMap.put(pid, mapList);
            }
        }
        return Result.ok(listMap);
    }

    /**
     * 查询用户拥有的权限
     *
     * @apiNote 包括菜单权限和按钮权限
     */
    @GetMapping("/getUserPermissionByToken")
    public Result<?> getUserPermissionByToken() {
        LoginUser loginUser = SystemContextUtil.currentLoginUser();
        List<SysPermission> metaList = service.queryByUser(loginUser.getUsername());
        // 添加首页路由
        if (!PermissionDataUtil.hasIndexPage(metaList)) {
            SysPermission indexMenu = service.list(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getName, "首页")).get(0);
            metaList.add(0, indexMenu);
        }
        JSONObject returnData = JSONUtil.createObj();
        JSONArray menuJsonArray = JSONUtil.createArray();
        getPermissionJsonArray(menuJsonArray, metaList, null);
        JSONArray authJsonArray = JSONUtil.createArray();
        getAuthJsonArray(authJsonArray, metaList);
        //查询所有的权限
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.eq(SysPermission::getMenuType, CommonConst.MENU_TYPE_2);
        //query.eq(SysPermission::getStatus, "1");
        List<SysPermission> allAuthList = service.list(query);
        JSONArray allAuthJsonArray = JSONUtil.createArray();
        getAllAuthJsonArray(allAuthJsonArray, allAuthList);
        //路由菜单
        returnData.set("menu", menuJsonArray);
        //按钮权限（用户拥有的权限集合）
        returnData.set("auth", authJsonArray);
        //全部权限配置集合（按钮权限，访问权限）
        returnData.set("allAuth", allAuthJsonArray);
        return Result.ok(returnData);
    }

    /**
     * 添加菜单
     *
     * @param permission 菜单、按钮参数
     */
    @AutoLog("菜单管理-添加菜单/按钮权限")
    @PostMapping("/add")
    public Result<?> add(@RequestBody SysPermission permission) {
        permission = PermissionDataUtil.intelligentProcessData(permission);
        service.addPermission(permission);
        return Result.ok();
    }

    /**
     * 编辑菜单
     *
     * @param permission 菜单、按钮参数
     */
    @AutoLog("菜单管理-编辑菜单/按钮权限")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysPermission permission) {
        permission = PermissionDataUtil.intelligentProcessData(permission);
        service.editPermission(permission);
        return Result.ok();
    }

    /**
     * 删除菜单
     *
     * @param id 主键id
     */
    @AutoLog("菜单管理-删除菜单/按钮权限")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        service.deletePermission(id);
        return Result.ok();
    }

    /**
     * 批量删除菜单
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @AutoLog("菜单管理-批量删除菜单/按钮权限")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        String[] arr = ids.split(",");
        for (String id : arr) {
            if (StrUtil.isNotEmpty(id)) {
                service.deletePermission(id);
            }
        }
        return Result.ok();
    }

    /**
     * 获取全部的权限树
     */
    @GetMapping("/queryTreeList")
    public Result<?> queryTreeList() {
        List<String> ids = new ArrayList<>();
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysPermission::getSortNo);
        List<SysPermission> list = service.list(query);
        for (SysPermission sysPer : list) {
            ids.add(sysPer.getId());
        }
        List<TreeModel> treeList = new ArrayList<>();
        getTreeModelList(treeList, list, null);
        Map<String, Object> resMap = new HashMap<>();
        // 全部树节点数据
        resMap.put("treeList", treeList);
        // 全部树ids
        resMap.put("ids", ids);
        return Result.ok(resMap);
    }

    /**
     * 异步加载数据节点
     *
     * @param parentId 父id
     */
    @GetMapping("/queryListAsync")
    public Result<List<TreeModel>> queryAsync(@RequestParam(required = false) String parentId) {
        List<TreeModel> list = service.queryListByParentId(parentId);
        return Result.ok(list);
    }

    /**
     * 查询角色授权
     *
     * @param roleId 角色id
     */
    @GetMapping("/queryRolePermission")
    public Result<List<String>> queryRolePermission(@RequestParam String roleId) {
        List<SysRolePermission> list = sysRolePermissionService.list(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId));
        return Result.ok(list.stream().map(SysRolePermission -> String.valueOf(SysRolePermission.getPermissionId())).collect(Collectors.toList()));
    }

    /**
     * 保存角色授权
     *
     * @param json 参数
     */
    @AutoLog("保存角色授权")
    @PostMapping("/saveRolePermission")
    public Result<String> saveRolePermission(@RequestBody JsonNode json) {
        String roleId = json.get("roleId").asText();
        String permissionIds = json.get("permissionIds").asText();
        String lastPermissionIds = json.get("lastpermissionIds").asText();
        sysRolePermissionService.saveRolePermission(roleId, permissionIds, lastPermissionIds);
        return Result.ok();
    }

    /**
     * 根据菜单id来获取其对应的权限数据
     *
     * @param sysPermissionDataRule 查询参数
     */
    @GetMapping("/getPermRuleListByPermId")
    public Result<List<SysPermissionDataRule>> getPermRuleListByPermId(SysPermissionDataRule sysPermissionDataRule) {
        List<SysPermissionDataRule> permRuleList = sysPermissionDataRuleService.getPermRuleListByPermId(sysPermissionDataRule.getPermissionId());
        return Result.ok(permRuleList);
    }

    /**
     * 添加数据权限规则
     *
     * @param sysPermissionDataRule 数据权限参数
     */
    @AutoLog("添加数据权限规则")
    @PostMapping("/addPermissionRule")
    public Result<?> addPermissionRule(@RequestBody SysPermissionDataRule sysPermissionDataRule) {
        sysPermissionDataRuleService.savePermissionDataRule(sysPermissionDataRule);
        return Result.ok();
    }

    /**
     * 修改数据权限规则
     *
     * @param sysPermissionDataRule 修改参数
     */
    @AutoLog("修改数据权限规则")
    @PutMapping("/editPermissionRule")
    public Result<?> editPermissionRule(@RequestBody SysPermissionDataRule sysPermissionDataRule) {
        sysPermissionDataRuleService.saveOrUpdate(sysPermissionDataRule);
        return Result.ok();
    }

    /**
     * 删除菜单权限数据
     *
     * @param id 主键id
     */
    @AutoLog("删除菜单权限数据")
    @DeleteMapping("/deletePermissionRule")
    public Result<?> deletePermissionRule(@RequestParam String id) {
        sysPermissionDataRuleService.deletePermissionDataRule(id);
        return Result.ok();
    }

    /**
     * 查询菜单权限数据
     *
     * @param sysPermissionDataRule 查询参数
     */
    @GetMapping("/queryPermissionRule")
    public Result<List<SysPermissionDataRule>> queryPermissionRule(SysPermissionDataRule sysPermissionDataRule) {
        List<SysPermissionDataRule> permRuleList = sysPermissionDataRuleService.queryPermissionRule(sysPermissionDataRule);
        return Result.ok(permRuleList);
    }

    /**
     * 查询部门权限
     *
     * @param departId 部门id
     */
    @GetMapping("/queryDepartPermission")
    public Result<List<String>> queryDepartPermission(@RequestParam String departId) {
        List<SysDepartPermission> list = sysDepartPermissionService.list(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, departId));
        return Result.ok(list.stream().map(SysDepartPermission -> String.valueOf(SysDepartPermission.getPermissionId())).collect(Collectors.toList()));
    }

    /**
     * 保存部门授权
     *
     * @param json 参数
     */
    @AutoLog("保存部门授权")
    @PostMapping("/saveDepartPermission")
    public Result<String> saveDepartPermission(@RequestBody JsonNode json) {
        String departId = json.get("departId").asText();
        String permissionIds = json.get("permissionIds").asText();
        String lastPermissionIds = json.get("lastpermissionIds").asText();
        sysDepartPermissionService.saveDepartPermission(departId, permissionIds, lastPermissionIds);
        return Result.ok();
    }

    private void getTreeList(List<SysPermissionTree> treeList, List<SysPermission> metaList, SysPermissionTree temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            SysPermissionTree tree = new SysPermissionTree(permission);
            if (temp == null && StrUtil.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.isLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
                temp.getChildren().add(tree);
                if (!tree.isLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            }
        }
    }

    private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission);
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

    /**
     * 获取权限JSON数组
     */
    private void getAllAuthJsonArray(JSONArray jsonArray, List<SysPermission> allList) {
        for (SysPermission permission : allList) {
            JSONObject json = JSONUtil.createObj();
            json.set("action", permission.getPerms());
            json.set("status", permission.getStatus());
            json.set("type", permission.getPermsType());
            json.set("describe", permission.getName());
            jsonArray.add(json);
        }
    }

    /**
     * 获取权限JSON数组
     */
    private void getAuthJsonArray(JSONArray jsonArray, List<SysPermission> metaList) {
        for (SysPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            if (permission.getMenuType().equals(CommonConst.MENU_TYPE_2) && CommonConst.STATUS_1.equals(permission.getStatus())) {
                JSONObject json = JSONUtil.createObj();
                json.set("action", permission.getPerms());
                json.set("type", permission.getPermsType());
                json.set("describe", permission.getName());
                jsonArray.add(json);
            }
        }
    }

    /**
     * 获取菜单JSON数组
     */
    private void getPermissionJsonArray(JSONArray jsonArray, List<SysPermission> metaList, JSONObject parentJson) {
        for (SysPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            String tempPid = permission.getParentId();
            JSONObject json = getPermissionJsonObject(permission);
            if (json == null) {
                continue;
            }
            if (parentJson == null && StrUtil.isEmpty(tempPid)) {
                jsonArray.add(json);
                if (!permission.isLeaf()) {
                    getPermissionJsonArray(jsonArray, metaList, json);
                }
            } else if (parentJson != null && StrUtil.isNotEmpty(tempPid) && tempPid.equals(parentJson.getStr("id"))) {
                // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                if (permission.getMenuType().equals(CommonConst.MENU_TYPE_2)) {
                    JSONObject metaJson = parentJson.getJSONObject("meta");
                    if (metaJson.containsKey("permissionList")) {
                        JSONArray arrayNode = metaJson.getJSONArray("permissionList");
                        arrayNode.add(json);
                    } else {
                        JSONArray permissionList = JSONUtil.createArray();
                        permissionList.add(json);
                        metaJson.set("permissionList", permissionList);
                    }
                    // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                } else if (permission.getMenuType().equals(CommonConst.MENU_TYPE_1) || permission.getMenuType().equals(CommonConst.MENU_TYPE_0)) {
                    if (parentJson.containsKey("children")) {
                        parentJson.getJSONArray("children").add(json);
                    } else {
                        JSONArray children = JSONUtil.createArray();
                        children.add(json);
                        parentJson.set("children", children);
                    }
                    if (!permission.isLeaf()) {
                        getPermissionJsonArray(jsonArray, metaList, json);
                    }
                }
            }
        }
    }

    /**
     * 根据菜单配置生成路由json
     */
    private JSONObject getPermissionJsonObject(SysPermission permission) {
        JSONObject json = JSONUtil.createObj();
        // 类型(0：一级菜单 1：子菜单 2：按钮)
        if (permission.getMenuType().equals(CommonConst.MENU_TYPE_2)) {
            //json.put("action", permission.getPerms());
            //json.put("type", permission.getPermsType());
            //json.put("describe", permission.getName());
            return null;
        } else if (permission.getMenuType().equals(CommonConst.MENU_TYPE_0) || permission.getMenuType().equals(CommonConst.MENU_TYPE_1)) {
            json.set("id", permission.getId());
            if (permission.isRoute()) {
                json.set("route", "1");// 表示生成路由
            } else {
                json.set("route", "0");// 表示不生成路由
            }
            if (isWWWHttpUrl(permission.getUrl())) {
                json.set("path", SecureUtil.md5(permission.getUrl()));
            } else {
                json.set("path", permission.getUrl());
            }
            // 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
            if (StrUtil.isNotEmpty(permission.getComponentName())) {
                json.set("name", permission.getComponentName());
            } else {
                json.set("name", urlToRouteName(permission.getUrl()));
            }
            // 是否隐藏路由，默认都是显示的
            if (permission.isHidden()) {
                json.set("hidden", true);
            }
            // 聚合路由
            if (permission.isAlwaysShow()) {
                json.set("alwaysShow", true);
            }
            json.set("component", permission.getComponent());
            JSONObject meta = JSONUtil.createObj();
            // 由用户设置是否缓存页面 用布尔值
            meta.set("keepAlive", permission.isKeepAlive());
            /*update_begin author:wuxianquan date:20190908 for:往菜单信息里添加外链菜单打开方式 */
            //外链菜单打开方式
            meta.set("internalOrExternal", permission.isInternalOrExternal());
            /* update_end author:wuxianquan date:20190908 for: 往菜单信息里添加外链菜单打开方式*/
            meta.set("title", permission.getName());
            //update-begin--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
            String component = permission.getComponent();
            if (StrUtil.isNotEmpty(permission.getComponentName()) || StrUtil.isNotEmpty(component)) {
                String componentName = permission.getComponentName();
                if (StrUtil.isEmpty(permission.getComponentName())) {
                    componentName = component.substring(component.lastIndexOf("/") + 1);
                }
                meta.set("componentName", componentName);
            }
            //update-end--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
            if (StrUtil.isEmpty(permission.getParentId())) {
                // 一级菜单跳转地址
                json.set("redirect", permission.getRedirect());
            }
            if (StrUtil.isNotEmpty(permission.getIcon())) {
                meta.set("icon", permission.getIcon());
            }
            if (isWWWHttpUrl(permission.getUrl())) {
                meta.set("url", permission.getUrl());
            }
            json.set("meta", meta);
        }
        return json;
    }

    /**
     * 判断是否外网URL
     *
     * @apiNote 例如： <a href="http://localhost:8080/jeecg-boot/swagger-ui.html">http://localhost:8080/jeecg-boot/swagger-ui.html</a> 支持特殊格式： {{window._CONFIG['domianURL'] }}/druid/ {{ JS代码片段 }}，前台解析会自动执行JS代码片段
     */
    private boolean isWWWHttpUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("{{"));
    }

    /**
     * 通过URL生成路由name
     *
     * @apiNote 去掉URL前缀斜杠，替换内容中的斜杠‘/’为‘-‘，举例： URL = /isystem/role RouteName = isystem-role
     */
    private String urlToRouteName(String url) {
        if (StrUtil.isNotEmpty(url)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = url.replace("/", "-");

            // 特殊标记
            url = url.replace(":", "@");
            return url;
        } else {
            return null;
        }
    }
}
