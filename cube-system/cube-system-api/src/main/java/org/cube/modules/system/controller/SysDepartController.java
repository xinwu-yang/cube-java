package org.cube.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.modules.system.service.ISysDepartService;
import org.cube.modules.system.service.ISysUserDepartService;
import org.cube.modules.system.service.ISysUserService;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.YouBianCodeUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.SysDepart;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.model.DepartIdModel;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.model.SysDepartTreeModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 部门
 *
 * @author xinwuy
 * @since 2021-05-25
 */
@Slf4j
@Tag(name = "部门管理相关接口")
@RestController
@RequestMapping("/sys/depart")
public class SysDepartController extends CubeController<SysDepart, ISysDepartService> {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;

    /**
     * 查询我的部门
     *
     * @apiNote 以树结构方式返回，上级才返回数据
     */
    @GetMapping("/queryMyDeptTreeList")
    @Operation(summary = "查询我的部门")
    public Result<List<SysDepartTreeModel>> queryMyDeptTreeList() {
        LoginUser user = SystemContextUtil.currentLoginUser();
        if (user.getUserIdentity() != null && user.getUserIdentity().equals(CommonConst.USER_IDENTITY_2)) {
            List<SysDepartTreeModel> list = service.queryMyDeptTreeList(user.getDepartIds());
            return Result.ok(user.getUserIdentity().toString(), list);
        }
        return Result.ok(user.getUserIdentity().toString(), null);
    }

    /**
     * 查询所有部门
     *
     * @return 部门树
     * @apiNote 以树结构方式返回
     */
    @GetMapping("/queryTreeList")
    @Operation(summary = "查询所有部门")
    public Result<List<SysDepartTreeModel>> queryTreeList(boolean myself) {
        List<SysDepartTreeModel> list;
        if (myself) {
            LoginUser user = SystemContextUtil.currentLoginUser();
            list = service.queryMyDeptTreeList(user.getDepartIds());
        } else {
            list = service.queryTreeList();
        }
        return Result.ok(list);
    }

    /**
     * 新增部门
     *
     * @param sysDepart 部门信息
     */
    @AutoLog("新增部门")
    @PostMapping("/add")
    @Operation(summary = "新增部门")
    public Result<?> add(@RequestBody SysDepart sysDepart) {
        return service.saveDepartData(sysDepart);
    }

    /**
     * 更新部门信息
     *
     * @param sysDepart 部门信息
     */
    @AutoLog("更新部门信息")
    @PutMapping("/edit")
    @Operation(summary = "更新部门信息")
    public Result<?> edit(@RequestBody SysDepart sysDepart) {
        String username = StpUtil.getLoginIdAsString();
        sysDepart.setUpdateBy(username);
        SysDepart sysDepartEntity = service.getById(sysDepart.getId());
        if (sysDepartEntity == null) {
            return Result.error("未找到对应数据！");
        }
        service.updateDepartDataById(sysDepart, username);
        return Result.ok();
    }

    /**
     * 删除部门
     *
     * @param id 主键id
     */
    @AutoLog("删除部门")
    @DeleteMapping("/delete")
    @Operation(summary = "删除部门")
    public Result<?> delete(@RequestParam String id) {
        SysDepart sysDepart = service.getById(id);
        if (sysDepart == null) {
            return Result.error("未找到对应数据！");
        }
        service.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除部门
     *
     * @param ids id数组（逗号分割）
     */
    @AutoLog("批量删除部门")
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "批量删除部门")
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.error("参数不识别！");
        }
        service.deleteBatchWithChildren(Arrays.asList(ids.split(",")));
        return Result.ok();
    }

    /**
     * 查询部门信息
     *
     * @apiNote 添加或编辑页面对该方法发起请求, 以树结构形式加载所有部门的名称, 方便用户的操作
     */
    @GetMapping("/queryIdTree")
    @Operation(summary = "查询部门信息")
    public Result<List<DepartIdModel>> queryIdTree() {
        List<DepartIdModel> list = service.queryDepartIdTreeList();
        return Result.ok(list);
    }

    /**
     * 部门搜索
     *
     * @param keyWord      关键词
     * @param myDeptSearch 我的部门下搜索
     * @return 部门列表树
     */
    @GetMapping("/searchBy")
    @Operation(summary = "部门搜索")
    public Result<List<SysDepartTreeModel>> searchBy(@Parameter(name = "关键词") String keyWord, @Parameter(name = "我的部门下搜索") @RequestParam(required = false) String myDeptSearch) {
        //部门查询，myDeptSearch为1时为我的部门查询，登录用户为上级时查只查负责部门下数据
        LoginUser user = SystemContextUtil.currentLoginUser();
        String departIds = null;
        if (user.getUserIdentity() != null && user.getUserIdentity().equals(CommonConst.USER_IDENTITY_2)) {
            departIds = user.getDepartIds();
        }
        List<SysDepartTreeModel> treeList = service.searchBy(keyWord, myDeptSearch, departIds);
        if (treeList == null || treeList.size() == 0) {
            return Result.error("未查询匹配数据！");
        }
        return Result.ok(treeList);
    }

    /**
     * 导出部门数据为Excel
     *
     * @param sysDepart 查询参数
     */
    @GetMapping("/exportXls")
    @Operation(summary = "导出Excel")
    public void exportXls(SysDepart sysDepart, HttpServletRequest request, HttpServletResponse response) throws IOException {
        QueryWrapper<SysDepart> queryWrapper = QueryGenerator.initQueryWrapper(sysDepart, request.getParameterMap());
        List<SysDepart> pageList = service.list(queryWrapper);
        //按字典排序
        pageList.sort(Comparator.comparing(SysDepart::getOrgCode));
        //导出文件名称
        String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        HttpServletUtil.addDownloadHeader(response, "部门数据-" + date + easyExcel.getExtension());
        easyExcel.export(pageList, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * 通过Excel导入部门数据
     */
    @PostMapping("/importExcel")
    @AutoLog("部门管理-通过Excel导入部门数据")
    @Operation(summary = "Excel导入")
    @CacheEvict(cacheNames = CacheConst.SYS_DEPARTS_CACHE, allEntries = true)
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<String> errorMessages = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        List<SysDepart> listSysDeparts;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try (InputStream inputStream = file.getInputStream()) {
                ImportExcel excel = new ImportExcel();
                excel.setInputStream(inputStream);
                // orgCode编码长度
                int codeLength = YouBianCodeUtil.zhanweiLength;
                listSysDeparts = easyExcel.read(SysDepart.class, excel, SystemContextUtil.dictTranslator());
                //按长度排序
                listSysDeparts.sort(Comparator.comparingInt(arg0 -> arg0.getOrgCode().length()));
                for (SysDepart sysDepart : listSysDeparts) {
                    String orgCode = sysDepart.getOrgCode();
                    if (orgCode.length() > codeLength) {
                        String parentCode = orgCode.substring(0, orgCode.length() - codeLength);
                        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("org_code", parentCode);
                        SysDepart parentDept = service.getOne(queryWrapper);
                        if (parentDept != null) {
                            sysDepart.setParentId(parentDept.getId());
                        } else {
                            sysDepart.setParentId("");
                        }
                    } else {
                        sysDepart.setParentId("");
                    }
                    sysDepart.setOrgType(sysDepart.getOrgCode().length() / codeLength + "");
                    sysDepart.setDelFlag(CommonConst.NOT_DELETED);
                    try {
                        service.save(sysDepart);
                        successLines++;
                    } catch (Exception e) {
                        errorLines++;
                        int lineNumber = 1;
                        errorMessages.add("第 " + lineNumber + " 行：存在错误，忽略导入。【" + e.getMessage() + "】");
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
     * 查询所有部门信息
     *
     * @param id 查询指定多个id的部门信息
     * @return 部门信息
     */
    @DictMethod
    @Deprecated
    @GetMapping("/listAll")
    @Operation(summary = "查询所有部门信息")
    public Result<List<SysDepart>> listAll(@Parameter(name = "部门id", description = "指定多个逗号分隔") @RequestParam(required = false) String id) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.orderByAsc(SysDepart::getOrgCode);
        if (StrUtil.isNotEmpty(id)) {
            Object[] arr = id.split(",");
            query.in(SysDepart::getId, arr);
        }
        List<SysDepart> sysDeparts = service.list(query);
        return Result.ok(sysDeparts);
    }

    /**
     * 根据关键词查询部门信息
     *
     * @param keyWord 关键词
     * @apiNote 以树结构方式返回
     */
    @Deprecated
    @GetMapping("/queryTreeByKeyWord")
    @Operation(summary = "根据关键词查询部门信息")
    public Result<?> queryTreeByKeyWord(@Parameter(name = "关键词") @RequestParam(required = false) String keyWord) {
        List<SysDepartTreeModel> list = service.queryTreeByKeyWord(keyWord);
        //根据keyWord获取用户信息
        LambdaQueryWrapper<SysUser> queryUser = new LambdaQueryWrapper<>();
        queryUser.eq(SysUser::getDelFlag, CommonConst.NOT_DELETED);
        queryUser.and(i -> i.like(SysUser::getUsername, keyWord).or().like(SysUser::getRealname, keyWord));
        List<SysUser> sysUsers = sysUserService.list(queryUser);
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("userList", sysUsers);
        returnData.put("departList", list);
        return Result.ok(returnData);
    }

    /**
     * 根据部门编码获取部门信息
     *
     * @param orgCode 部门编码
     * @return 部门信息
     */
    @Deprecated
    @DictMethod
    @GetMapping("/getDepartName")
    @Operation(summary = "根据部门编码获取部门信息")
    public Result<SysDepart> getDepartName(@Parameter(name = "部门编码") @RequestParam String orgCode) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getOrgCode, orgCode);
        SysDepart sysDepart = service.getOne(query);
        return Result.ok(sysDepart);
    }

    /**
     * 根据部门id获取用户信息
     *
     * @param id 部门id
     * @return 用户列表
     */
    @Deprecated
    @GetMapping("/getUsersByDepartId")
    @Operation(summary = "根据部门id获取用户信息")
    public Result<List<SysUser>> getUsersByDepartId(@RequestParam String id) {
        List<SysUser> sysUsers = sysUserDepartService.queryUserByDepId(id);
        return Result.ok(sysUsers);
    }
}
