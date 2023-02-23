package org.cube.modules.system.enhancement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictApi;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.controller.response.AddKeyResponse;
import org.cube.modules.system.enhancement.entity.SysRoleKey;
import org.cube.modules.system.enhancement.service.ISysRoleKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * API密钥管理
 *
 * @author 杨欣武
 * @version V2.0.0
 * @since 2022-08-02
 */
@Slf4j
@Tag(name = "API密钥管理")
@DictApi
@RestController
@RequestMapping("/sys/ram")
public class SysRoleKeyController extends CubeController<SysRoleKey, ISysRoleKeyService> {

    /**
     * 分页列表查询
     */
    @GetMapping("/list")
    @Operation(summary = "分页列表查询")
    public Result<?> queryPageList(SysRoleKey sysRoleKey, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysRoleKey> queryWrapper = QueryGenerator.initQueryWrapper(sysRoleKey, req.getParameterMap());
        Page<SysRoleKey> page = new Page<>(pageNo, pageSize);
        IPage<SysRoleKey> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     */
    @AutoLog("API密钥管理-新增")
    @PostMapping("/add")
    @Operation(summary = "新增")
    public Result<AddKeyResponse> add(@RequestBody SysRoleKey sysRoleKey) {
        AddKeyResponse response = service.addRoleKey(sysRoleKey);
        return Result.ok(response);
    }

    /**
     * 修改
     */
    @AutoLog("API密钥管理-修改")
    @PutMapping("/edit")
    @Operation(summary = "修改")
    public Result<?> edit(@RequestBody SysRoleKey sysRoleKey) {
        service.updateById(sysRoleKey);
        return Result.ok();
    }

    /**
     * 签出API凭证
     */
    @AutoLog("API密钥管理-签出API凭证")
    @GetMapping("/sign")
    @Operation(summary = "签出API凭证")
    public Result<?> sign(@RequestParam String signature) {
        String[] params = signature.split("\\.");
        if (params.length < 2) {
            return Result.error("请求参数有误！");
        }
        return service.sign(params[0], params[1]);
    }

    /**
     * 通过id删除
     */
    @AutoLog("API密钥管理-通过id删除")
    @DeleteMapping("/delete")
    @Operation(summary = "通过id删除")
    public Result<?> delete(@RequestParam String id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     */
    @AutoLog("API密钥管理-批量删除")
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "批量删除")
    public Result<?> deleteBatch(@RequestParam String ids) {
        service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok();
    }

    /**
     * 通过id查询
     */
    @GetMapping("/queryById")
    @Operation(summary = "通过id查询")
    public Result<?> queryById(@RequestParam String id) {
        SysRoleKey sysRoleKey = service.getById(id);
        return Result.ok(sysRoleKey);
    }

    /**
     * 导出Excel
     */
    @RequestMapping("/exportXls")
    @Operation(summary = "导出Excel")
    public void exportXls(HttpServletRequest request, HttpServletResponse response, SysRoleKey sysRoleKey) throws IOException {
        super.exportXls(request, response, sysRoleKey, "API密钥管理");
    }

    /**
     * Excel导入
     */
    @PostMapping("/importExcel")
    @Operation(summary = "Excel导入")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        return super.importExcel(request, SysRoleKey.class);
    }
}