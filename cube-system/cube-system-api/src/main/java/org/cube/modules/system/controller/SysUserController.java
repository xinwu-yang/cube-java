package org.cube.modules.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictApi;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.CubeController;
import org.cube.commons.base.Result;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.enums.OperateLogType;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.crypto.PasswordUtil;
import org.cube.commons.utils.spring.RedisUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.*;
import org.cube.modules.system.model.DepartIdModel;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.model.SysUserSysDepartModel;
import org.cube.modules.system.model.api.request.AddSysUserRoleRequest;
import org.cube.modules.system.model.api.request.EditSysDepartWithUserRequest;
import org.cube.modules.system.model.api.request.UpdateMobileRequest;
import org.cube.modules.system.service.*;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2021-05-28
 */
@Slf4j
@Tag(name = "用户管理相关接口")
@DictApi
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends CubeController<SysUser, ISysUserService> {

    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;
    @Autowired
    private ISysDepartRoleUserService departRoleUserService;
    @Autowired
    private ISysDepartRoleService departRoleService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取用户列表数据
     *
     * @param user     查询条件
     * @param pageNo   页码
     * @param pageSize 每页条数
     */
    @DictMethod
    @GetMapping("/list")
    public Result<IPage<SysUser>> queryPageList(SysUser user, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        queryWrapper.ne("username", "_reserve_user_external");
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        IPage<SysUser> pageList = service.page(page, queryWrapper);
        // 批量查询用户的所属部门
        List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        if (userIds.size() > 0) {
            Map<String, String> useDepNames = service.getDepNamesByUserIds(userIds);
            pageList.getRecords().forEach(item -> item.setOrgCodeTxt(useDepNames.get(item.getId())));
        }
        return Result.ok(pageList);
    }

    /**
     * 新增用户
     *
     * @param params 新增参数
     */
    @AutoLog("用户管理-新增用户")
    @PostMapping("/add")
    public Result<?> add(@RequestBody JsonNode params) {
        JsonNode roles = params.get("selectedroles");
        JsonNode departs = params.get("selecteddeparts");
        String selectedRoles = roles != null ? roles.asText() : null;
        String selectedDeparts = departs != null ? departs.asText() : null;
        SysUser user = JSONUtil.toBean(params.toString(), SysUser.class);
        service.add(user, selectedRoles, selectedDeparts);
        return Result.ok();
    }

    /**
     * 更新
     *
     * @param jsonObject 参数
     */
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody JsonNode jsonObject) {
        SysUser sysUser = service.getById(jsonObject.get("id").asText());
        if (sysUser == null) {
            return Result.error("未找到对应实体！");
        }
        SysUser user = JSONUtil.toBean(jsonObject.toString(), SysUser.class);
        user.setUpdateTime(new Date());
        //String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
        user.setPassword(sysUser.getPassword());
        String roles = jsonObject.get("selectedroles").asText();
        String departs = jsonObject.get("selecteddeparts").asText();
        service.editUserWithRole(user, roles);
        service.editUserWithDepart(user, departs);
        service.updateNullPhoneEmail();
        SystemContextUtil.log("编辑用户，id： " + jsonObject.get("id").asText(), OperateLogType.UPDATE);
        return Result.ok();
    }

    /**
     * 删除用户
     *
     * @param id 主键id
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        service.deleteUser(id);
        return Result.ok();
    }

    /**
     * 批量删除用户
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        service.deleteBatchUsers(ids);
        return Result.ok();
    }

    /**
     * 冻结或解冻用户
     *
     * @param jsonObject status (1 解冻 、2 冻结)
     */
    @AutoLog("用户管理-冻结/解冻用户")
    @PutMapping("/frozenBatch")
    public Result<SysUser> frozenBatch(@RequestBody JsonNode jsonObject) {
        String ids = jsonObject.get("ids").asText();
        String status = jsonObject.get("status").asText();
        String[] arr = ids.split(",");
        for (String id : arr) {
            if (StrUtil.isNotEmpty(id)) {
                service.update(new SysUser().setStatus(Integer.parseInt(status)), new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId, id));
            }
        }
        return Result.ok();
    }

    /**
     * 根据id查询用户角色
     *
     * @param userId 用户id
     */
    @GetMapping("/queryUserRole")
    public Result<List<String>> queryUserRole(@RequestParam String userId) {
        List<String> list = new ArrayList<>();
        List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId));
        if (userRole == null || userRole.size() <= 0) {
            return Result.error("未找到用户相关角色信息！");
        }
        for (SysUserRole sysUserRole : userRole) {
            list.add(sysUserRole.getRoleId());
        }
        return Result.ok(list);
    }

    /**
     * 校验用户账号是否唯一
     *
     * @param sysUser 用户信息
     */
    @GetMapping("/checkOnlyUser")
    public Result<?> checkOnlyUser(SysUser sysUser) {
        //通过传入信息查询新的用户信息
        SysUser user = service.getOne(new QueryWrapper<>(sysUser));
        if (user != null) {
            return Result.error("用户账号已存在！");
        }
        return Result.ok(true);
    }

    /**
     * 管理员修改密码
     */
    @SaCheckRole("admin")
    @PutMapping("/changePassword")
    public Result<?> changePassword(@RequestBody SysUser sysUser) {
        SysUser user = service.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
        if (user == null) {
            return Result.error("用户不存在！");
        }
        sysUser.setId(user.getId());
        return service.changePassword(sysUser);
    }

    /**
     * 查询指定用户和部门关联的数据
     *
     * @param userId 用户id
     */
    @GetMapping("/userDepartList")
    public Result<List<DepartIdModel>> getUserDepartsList(@RequestParam String userId) {
        List<DepartIdModel> depIdModelList = sysUserDepartService.queryDepartIdsOfUser(userId);
        if (depIdModelList == null || depIdModelList.size() == 0) {
            return Result.error("未绑定部门！");
        }
        return Result.ok("查找成功！", depIdModelList);
    }

    /**
     * 生成在添加用户情况下没有主键的问题
     *
     * @return 返回给前端根据该id绑定部门数据
     */
    @GetMapping("/generateUserId")
    public Result<?> generateUserId() {
        String userId = UUID.randomUUID().toString().replace("-", "");
        return Result.ok(userId);
    }

    /**
     * 根据部门id查询用户信息
     *
     * @param id       部门id
     * @param realname 用户真实姓名
     */
    @GetMapping("/queryUserByDepId")
    public Result<List<SysUser>> queryUserByDepId(@RequestParam String id, @RequestParam(required = false) String realname) {
        SysDepart sysDepart = sysDepartService.getById(id);
        List<SysUser> userList = sysUserDepartService.queryUserByDepCode(sysDepart.getOrgCode(), realname);
        //批量查询用户的所属部门
        //step.1 先拿到全部的 useids
        //step.2 通过 useids，一次性查询用户的所属部门名字
        List<String> userIds = userList.stream().map(SysUser::getId).collect(Collectors.toList());
        if (userIds.size() > 0) {
            Map<String, String> useDepNames = service.getDepNamesByUserIds(userIds);
            userList.forEach(item -> {
                //TODO 临时借用这个字段用于页面展示
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
        return Result.ok(userList);
    }

    /**
     * 导出用户数据为Excel
     *
     * @param sysUser 查询条件
     */
    @GetMapping("/exportXls")
    public void exportXls(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, request.getParameterMap());
        String selections = request.getParameter("selections");
        if (StrUtil.isNotEmpty(selections)) {
            queryWrapper.in("id", ListUtil.toList(selections.split(",")));
        }
        List<SysUser> sysUsers = service.list(queryWrapper);
        //导出文件名称
        String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        HttpServletUtil.addDownloadHeader(response, "用户数据-" + date + easyExcel.getExtension());
        easyExcel.export(sysUsers, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * 通过Excel导入用户数据
     */
    @PostMapping("/importExcel")
    @AutoLog("用户管理-通过Excel导入用户数据")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        List<String> errorMessages = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try (InputStream inputStream = file.getInputStream()) {
                ImportExcel excel = new ImportExcel();
                excel.setInputStream(inputStream);
                List<SysUser> sysUsers = easyExcel.read(SysUser.class, excel, SystemContextUtil.dictTranslator());
                for (int i = 0; i < sysUsers.size(); i++) {
                    SysUser sysUser = sysUsers.get(i);
                    if (StrUtil.isBlank(sysUser.getPassword())) {
                        sysUser.setPassword(PasswordUtil.DEFAULT_PASSWORD);
                    }
                    // 密码加密加盐
                    if (StrUtil.isBlank(sysUser.getSalt())) {
                        String salt = RandomUtil.randomString(8);
                        sysUser.setPassword(salt);
                    }
                    String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), sysUser.getPassword(), sysUser.getSalt());
                    sysUser.setPassword(passwordEncode);
                    try {
                        service.save(sysUser);
                        successLines++;
                    } catch (Exception e) {
                        errorLines++;
                        String message = e.getMessage();
                        int lineNumber = i + 1;
                        // 通过索引名判断出错信息
                        if (message.contains(CommonConst.SQL_INDEX_UNIQ_SYS_USER_USERNAME)) {
                            errorMessages.add("第 " + lineNumber + " 行：用户名已经存在，忽略导入。");
                        } else if (message.contains(CommonConst.SQL_INDEX_UNIQ_SYS_USER_WORK_NO)) {
                            errorMessages.add("第 " + lineNumber + " 行：工号已经存在，忽略导入。");
                        } else if (message.contains(CommonConst.SQL_INDEX_UNIQ_SYS_USER_PHONE)) {
                            errorMessages.add("第 " + lineNumber + " 行：手机号已经存在，忽略导入。");
                        } else if (message.contains(CommonConst.SQL_INDEX_UNIQ_SYS_USER_EMAIL)) {
                            errorMessages.add("第 " + lineNumber + " 行：电子邮件已经存在，忽略导入。");
                        } else {
                            errorMessages.add("第 " + lineNumber + " 行：其他错误，忽略导入。【" + e.getMessage() + "】");
                        }
                    }
                    // 批量将部门和用户信息建立关联关系
                    String departIds = sysUser.getDepartIds();
                    if (StrUtil.isNotBlank(departIds)) {
                        String userId = sysUser.getId();
                        String[] departIdArray = departIds.split(",");
                        List<SysUserDepart> userDepartList = new ArrayList<>(departIdArray.length);
                        for (String departId : departIdArray) {
                            userDepartList.add(new SysUserDepart(userId, departId));
                        }
                        sysUserDepartService.saveBatch(userDepartList);
                    }
                }
            }
        }
        JSONObject returnData = JSONUtil.createObj();
        returnData.set("errorLines", errorLines);
        returnData.set("successLines", successLines);
        returnData.set("errorMessages", errorMessages);
        return Result.ok(returnData);
    }

    /**
     * 根据id批量查询
     *
     * @param userIds 用户id（多个逗号分隔）
     */
    @AutoLog("根据id批量查询")
    @GetMapping("/queryByIds")
    public Result<List<SysUser>> queryByIds(@RequestParam String userIds) {
        String[] userId = userIds.split(",");
        List<SysUser> userRole = service.listByIds(Arrays.asList(userId));
        return Result.ok(userRole);
    }

    /**
     * 用户自己修改密码
     */
    @AutoLog("用户自己修改密码")
    @PutMapping("/updatePassword")
    public Result<?> changPassword(@RequestBody JsonNode params) {
        String oldPassword = params.get("oldpassword").asText();
        String newPassword = params.get("password").asText();
        String confirmPassword = params.get("confirmpassword").asText();
        return service.changePassword(oldPassword, newPassword, confirmPassword);
    }

    /**
     * 根据角色和用户名查询用户
     *
     * @param pageNo   页码
     * @param pageSize 每页数量
     */
    @DictMethod
    @AutoLog("根据角色和用户名查询用户")
    @GetMapping("/userRoleList")
    public Result<IPage<SysUser>> userRoleList(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        String roleId = req.getParameter("roleId");
        String username = req.getParameter("username");
        IPage<SysUser> pageList = service.getUserByRoleId(page, roleId, username);
        return Result.ok(pageList);
    }

    /**
     * 给指定角色添加用户
     *
     * @param addSysUserRoleRequest 参数
     */
    @AutoLog("给指定角色添加用户")
    @PostMapping("/addSysUserRole")
    public Result<?> addSysUserRole(@RequestBody AddSysUserRoleRequest addSysUserRoleRequest) {
        String sysRoleId = addSysUserRoleRequest.getRoleId();
        for (String sysUserId : addSysUserRoleRequest.getUserIdList()) {
            SysUserRole sysUserRole = new SysUserRole(sysUserId, sysRoleId);
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", sysRoleId).eq("user_id", sysUserId);
            SysUserRole one = sysUserRoleService.getOne(queryWrapper);
            if (one == null) {
                sysUserRoleService.save(sysUserRole);
            }
        }
        return Result.ok();
    }

    /**
     * 删除指定角色的用户关系
     *
     * @param roleId 角色id
     * @param userId 用户id
     */
    @AutoLog("删除指定角色的用户关系")
    @DeleteMapping("/deleteUserRole")
    public Result<?> deleteUserRole(@RequestParam String roleId, @RequestParam String userId) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).eq("user_id", userId);
        sysUserRoleService.remove(queryWrapper);
        return Result.ok();
    }

    /**
     * 批量删除指定角色的用户关系
     *
     * @param roleId  角色id
     * @param userIds 用户id（多个逗号分隔）
     */
    @AutoLog("批量删除指定角色的用户关系")
    @DeleteMapping("/deleteUserRoleBatch")
    public Result<?> deleteUserRoleBatch(@RequestParam String roleId, @RequestParam String userIds) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).in("user_id", Arrays.asList(userIds.split(",")));
        sysUserRoleService.remove(queryWrapper);
        return Result.ok();
    }

    /**
     * 部门用户列表
     *
     * @param pageNo   页码
     * @param pageSize 每页数量
     */
    @DictMethod
    @AutoLog("部门用户列表")
    @GetMapping("/departUserList")
    public Result<IPage<SysUser>> departUserList(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        String depId = req.getParameter("depId");
        String username = req.getParameter("username");
        //根据部门ID查询,当前和下级所有的部门IDS
        List<String> subDepids = new ArrayList<>();
        //部门id为空时，查询我的部门下所有用户
        if (StrUtil.isEmpty(depId)) {
            LoginUser user = SystemContextUtil.currentLoginUser();
            int userIdentity = user.getUserIdentity() != null ? user.getUserIdentity() : CommonConst.USER_IDENTITY_1;
            if (userIdentity == CommonConst.USER_IDENTITY_2) {
                subDepids = sysDepartService.getMySubDepIdsByDepId(user.getDepartIds());
            }
        } else {
            subDepids = sysDepartService.getSubDepIdsByDepId(depId);
        }
        IPage<SysUser> pageList = null;
        if (subDepids != null && subDepids.size() > 0) {
            pageList = service.getUserByDepIds(page, subDepids, username);
            //批量查询用户的所属部门
            //step.1 先拿到全部的 useids
            //step.2 通过 useids，一次性查询用户的所属部门名字
            List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
            if (userIds.size() > 0) {
                Map<String, String> useDepNames = service.getDepNamesByUserIds(userIds);
                pageList.getRecords().forEach(item -> {
                    //批量查询用户的所属部门
                    item.setOrgCode(useDepNames.get(item.getId()));
                });
            }
        }
        return Result.ok(pageList);
    }

    /**
     * 根据orgCode查询用户
     *
     * @param pageNo     页码
     * @param pageSize   每页数量
     * @param orgCode    部门编码
     * @param userParams 查询参数
     * @apiNote 包括子部门下的用户，若某个用户包含多个部门，则会显示多条记录，可自行处理成单条记录
     */
    @GetMapping("/queryByOrgCode")
    public Result<IPage<SysUserSysDepartModel>> queryByDepartId(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam String orgCode, SysUser userParams) {
        IPage<SysUserSysDepartModel> pageList = new Page<>(pageNo, pageSize);
        pageList.setRecords(service.queryUserByOrgCode(orgCode, userParams, pageList));
        return Result.ok(pageList);
    }

    /**
     * 根据orgCode查询用户
     *
     * @param pageNo     页码
     * @param pageSize   每页数量
     * @param orgCode    部门编码
     * @param userParams 查询参数
     * @apiNote 包括子部门下的用户，针对通讯录模块做的接口，将多个部门的用户合并成一条记录，并转成对前端友好的格式
     */
    @GetMapping("/queryByOrgCodeForAddressList")
    public Result<IPage<SysUserSysDepartModel>> queryByOrgCodeForAddressList(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(required = false) String orgCode, SysUser userParams) {
        IPage<SysUserSysDepartModel> pageList = new Page<>(pageNo, pageSize);
        // 处理%_特殊字符串
        if (StrUtil.isNotBlank(userParams.getRealname())) {
            userParams.setRealname(QueryGenerator.specialStrConvert(userParams.getRealname()));
        }
        if (StrUtil.isNotBlank(userParams.getWorkNo())) {
            userParams.setWorkNo(QueryGenerator.specialStrConvert(userParams.getWorkNo()));
        }
        pageList.setRecords(service.queryUserByOrgCode(orgCode, userParams, pageList));
        return Result.ok(pageList);
    }

    /**
     * 给指定部门添加对应的用户
     *
     * @param editSysDepartWithUserRequest 查询参数
     */
    @AutoLog("给指定部门添加对应的用户")
    @PostMapping("/editSysDepartWithUser")
    public Result<?> editSysDepartWithUser(@RequestBody EditSysDepartWithUserRequest editSysDepartWithUserRequest) {
        String sysDepId = editSysDepartWithUserRequest.getDepId();
        for (String sysUserId : editSysDepartWithUserRequest.getUserIdList()) {
            SysUserDepart sysUserDepart = new SysUserDepart(null, sysUserId, sysDepId);
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dep_id", sysDepId).eq("user_id", sysUserId);
            SysUserDepart one = sysUserDepartService.getOne(queryWrapper);
            if (one == null) {
                sysUserDepartService.save(sysUserDepart);
            }
        }
        return Result.ok();
    }

    /**
     * 删除指定部门的用户关系
     *
     * @param depId  部门id
     * @param userId 用户id
     */
    @AutoLog("删除指定机构的用户关系")
    @DeleteMapping("/deleteUserInDepart")
    public Result<?> deleteUserInDepart(@RequestParam String depId, @RequestParam String userId) {
        QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_id", depId).eq("user_id", userId);
        boolean result = sysUserDepartService.remove(queryWrapper);
        if (!result) {
            return Result.error("当前选中部门与用户无关联关系！");
        }
        List<SysDepartRole> sysDepartRoleList = departRoleService.list(new QueryWrapper<SysDepartRole>().eq("depart_id", depId));
        List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            QueryWrapper<SysDepartRoleUser> query = new QueryWrapper<>();
            query.eq("user_id", userId).in("drole_id", roleIds);
            departRoleUserService.remove(query);
        }
        return Result.ok();
    }

    /**
     * 批量删除指定部门的用户关系
     *
     * @param depId   部门id
     * @param userIds 用户id（多个逗号分隔）
     */
    @AutoLog("批量删除指定机构的用户关系")
    @DeleteMapping("/deleteUserInDepartBatch")
    public Result<?> deleteUserInDepartBatch(@RequestParam String depId, @RequestParam String userIds) {
        QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_id", depId).in("user_id", Arrays.asList(userIds.split(",")));
        boolean result = sysUserDepartService.remove(queryWrapper);
        if (result) {
            departRoleUserService.removeDeptRoleUser(Arrays.asList(userIds.split(",")), depId);
        }
        return Result.ok();
    }

    /**
     * 查询当前用户的所有部门和部门编码
     */
    @GetMapping("/getCurrentUserDeparts")
    public Result<?> getCurrentUserDeparts() {
        LoginUser sysUser = SystemContextUtil.currentLoginUser();
        List<SysDepart> list = sysDepartService.queryUserDeparts(sysUser.getId());
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("list", list);
        returnData.put("orgCode", sysUser.getOrgCode());
        return Result.ok(returnData);
    }

    /**
     * 用户注册接口
     *
     * @param jsonObject 注册参数
     * @param user       用户信息
     */
    @AutoLog("用户注册接口")
    @PostMapping("/register")
    public Result<?> userRegister(@RequestBody JsonNode jsonObject, SysUser user) {
        String phone = jsonObject.get("phone").asText();
        String smscode = jsonObject.get("smscode").asText();
        Object code = redisUtil.get(phone);
        String username = jsonObject.get("username").asText();
        //未设置用户名，则用手机号作为用户名
        if (StrUtil.isEmpty(username)) {
            username = phone;
        }
        //未设置密码，则随机生成一个密码
        String password = jsonObject.get("password").asText();
        if (StrUtil.isEmpty(password)) {
            password = RandomUtil.randomString(8);
        }
        String email = jsonObject.get("email").asText();
        SysUser usernameCheck = service.getUserByName(username);
        if (usernameCheck != null) {
            return Result.error("用户名已注册！");
        }
        SysUser phoneCheck = service.getUserByPhone(phone);
        if (phoneCheck != null) {
            return Result.error("该手机号已注册！");
        }
        if (StrUtil.isNotEmpty(email)) {
            SysUser emailCheck = service.getUserByEmail(email);
            if (emailCheck != null) {
                return Result.error("邮箱已被注册！");
            }
        }
        if (null == code) {
            return Result.error("手机验证码失效，请重新获取！");
        }
        if (!smscode.equals(code)) {
            return Result.error("手机验证码错误！");
        }
        user.setCreateTime(new Date());// 设置创建时间
        String salt = RandomUtil.randomString(8);
        String passwordEncode = PasswordUtil.encrypt(username, password, salt);
        user.setSalt(salt);
        user.setUsername(username);
        user.setRealname(username);
        user.setPassword(passwordEncode);
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus(CommonConst.USER_UNFREEZE);
        user.setDelFlag(0);
        user.setFirstLogin(true);
        service.addUserWithRole(user, "1456085100369317890");//默认角色user
        return Result.ok();
    }

    /**
     * 根据用户名或手机号查询用户信息
     *
     * @param sysUser 查询参数
     */
    @GetMapping("/querySysUser")
    public Result<?> querySysUser(SysUser sysUser) {
        String phone = sysUser.getPhone();
        String username = sysUser.getUsername();
        Map<String, Object> returnData = new HashMap<>();
        if (StrUtil.isNotEmpty(phone)) {
            SysUser user = service.getUserByPhone(phone);
            if (user != null) {
                returnData.put("username", user.getUsername());
                returnData.put("phone", user.getPhone());
                return Result.ok(returnData);
            }
        }
        if (StrUtil.isNotEmpty(username)) {
            SysUser user = service.getUserByName(username);
            if (user != null) {
                returnData.put("username", user.getUsername());
                returnData.put("phone", user.getPhone());
                return Result.ok(returnData);
            }
        }
        return Result.error("未查询到结果！");
    }

    /**
     * 用户手机号验证
     *
     * @param jsonObject 参数
     */
    @PostMapping("/phoneVerification")
    public Result<?> phoneVerification(@RequestBody JsonNode jsonObject) {
        String phone = jsonObject.get("phone").asText();
        String smscode = jsonObject.get("smscode").asText();
        Object code = redisUtil.get(phone);
        if (!smscode.equals(code)) {
            return Result.error("手机验证码错误！");
        }
        //设置有效时间
        redisUtil.set(phone, smscode, 600);
        //新增查询用户名
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.eq(SysUser::getPhone, phone);
        SysUser user = service.getOne(query);
        Map<String, String> returnData = new HashMap<>();
        returnData.put("smscode", smscode);
        returnData.put("username", user.getUsername());
        return Result.ok(returnData);
    }

    /**
     * 重置密码
     *
     * @param username 用户名
     * @param password 新密码
     * @param smscode  验证码
     * @param phone    手机号
     */
    @AutoLog("重置密码")
    @GetMapping("/passwordChange")
    public Result<?> passwordChange(@RequestParam String username, @RequestParam String password, @RequestParam String smscode, @RequestParam String phone) {
        SysUser sysUser;
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password) || StrUtil.isEmpty(smscode) || StrUtil.isEmpty(phone)) {
            return Result.error("重置密码失败！");
        }
        Object object = redisUtil.get(phone);
        if (null == object) {
            return Result.error("短信验证码失效！");
        }
        if (!smscode.equals(object)) {
            return Result.error("短信验证码不匹配！");
        }
        sysUser = service.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username).eq(SysUser::getPhone, phone));
        if (sysUser == null) {
            return Result.error("未找到用户！");
        }
        String salt = RandomUtil.randomString(8);
        sysUser.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setPassword(passwordEncode);
        service.updateById(sysUser);
        return Result.ok();
    }

    /**
     * 获取被逻辑删除的用户列表
     */
    @GetMapping("/recycleBin")
    public Result<List<SysUser>> getRecycleBin() {
        List<SysUser> logicDeletedUserList = service.queryLogicDeleted();
        if (logicDeletedUserList.size() > 0) {
            // 批量查询用户的所属部门
            // step.1 先拿到全部的 userIds
            List<String> userIds = logicDeletedUserList.stream().map(SysUser::getId).collect(Collectors.toList());
            // step.2 通过 userIds，一次性查询用户的所属部门名字
            Map<String, String> useDepNames = service.getDepNamesByUserIds(userIds);
            logicDeletedUserList.forEach(item -> item.setOrgCode(useDepNames.get(item.getId())));
        }
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的用户
     *
     * @param jsonObject 参数
     */
    @AutoLog("还原被逻辑删除的用户")
    @PutMapping("/putRecycleBin")
    public Result<?> putRecycleBin(@RequestBody JsonNode jsonObject) {
        String userIds = jsonObject.get("userIds").asText();
        if (StrUtil.isNotBlank(userIds)) {
            SysUser updateUser = new SysUser();
            updateUser.setUpdateBy(StpUtil.getLoginIdAsString());
            updateUser.setUpdateTime(new Date());
            service.revertLogicDeleted(Arrays.asList(userIds.split(",")), updateUser);
        }
        return Result.ok();
    }

    /**
     * 彻底删除用户
     *
     * @param userIds 用户id（多个逗号分隔）
     */
    @SaCheckRole("admin")
    @AutoLog("彻底删除用户")
    @DeleteMapping("/deleteRecycleBin")
    public Result<?> deleteRecycleBin(@RequestParam String userIds) {
        if (StrUtil.isNotBlank(userIds)) {
            service.removeLogicDeleted(Arrays.asList(userIds.split(",")));
        }
        return Result.ok();
    }

    /**
     * 根据userId获取用户信息和部门员工信息
     *
     * @param userId 用户id
     */
    @Deprecated
    @GetMapping("/queryChildrenByUsername")
    public Result<?> queryChildrenByUsername(@RequestParam String userId) {
        //获取用户信息
        Map<String, Object> returnData = new HashMap<>();
        SysUser sysUser = service.getById(userId);
        String username = sysUser.getUsername();
        Integer identity = sysUser.getUserIdentity();
        returnData.put("sysUser", sysUser);
        if (identity != null && identity == 2) {
            //获取部门用户信息
            String departIds = sysUser.getDepartIds();
            if (StrUtil.isNotBlank(departIds)) {
                List<String> departIdList = Arrays.asList(departIds.split(","));
                List<SysUser> childrenUser = service.queryByDepIds(departIdList, username);
                returnData.put("children", childrenUser);
            }
        }
        return Result.ok(returnData);
    }

    /**
     * 更换手机号
     */
    @PutMapping("/updateMobile")
    @Operation(summary = "更换手机号")
    public Result<?> changMobile(@RequestBody UpdateMobileRequest request) {
        Object object = redisUtil.get(request.getPhone());
        if (null == object) {
            return Result.error("短信验证码失效！");
        }
        if (!object.equals(request.getCode())) {
            return Result.error("短信验证码错误！");
        }
        SysUser user = service.getUserByName(StpUtil.getLoginIdAsString());
        user.setPhone(request.getPhone());
        service.updateById(user);
        return Result.ok();
    }
}
