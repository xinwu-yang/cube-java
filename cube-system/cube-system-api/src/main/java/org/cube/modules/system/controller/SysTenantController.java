package org.cube.modules.system.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.annotations.PermissionData;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysTenant;
import org.cube.modules.system.service.ISysTenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 多租户
 */
@Slf4j
@Tag(name = "多租户相关接口")
@RestController
@RequestMapping("/sys/tenant")
public class SysTenantController extends CubeController<SysTenant, ISysTenantService> {

    /**
     * 分页列表查询
     *
     * @param sysTenant 查询参数
     * @param pageNo    页码
     * @param pageSize  每页数量
     */
    @DictMethod
    @GetMapping("/list")
    @PermissionData("system/TenantList")
    public Result<IPage<SysTenant>> queryPageList(SysTenant sysTenant, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysTenant> queryWrapper = QueryGenerator.initQueryWrapper(sysTenant, req.getParameterMap());
        Page<SysTenant> page = new Page<>(pageNo, pageSize);
        IPage<SysTenant> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     *
     * @param sysTenant 新增参数
     */
    @AutoLog("租户管理-新增")
    @PostMapping("/add")
    public Result<SysTenant> add(@RequestBody SysTenant sysTenant) {
        if (service.getById(sysTenant.getId()) != null) {
            return Result.error("该编号已存在！");
        }
        service.save(sysTenant);
        return Result.ok();
    }

    /**
     * 更新
     *
     * @param tenant 更新参数
     */
    @AutoLog("租户管理-更新")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysTenant tenant) {
        service.updateById(tenant);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 租户id
     */
    @AutoLog("租户管理-通过id删除")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     *
     * @param ids 租户id（多个逗号分隔）
     */
    @AutoLog("租户管理-批量删除")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (StrUtil.isEmpty(ids)) {
            return Result.error("未选中租户！");
        }
        List<String> list = Arrays.asList(ids.split(","));
        service.removeByIds(list);
        return Result.ok();
    }

    /**
     * 通过id查询
     *
     * @param id 租户id
     */
    @DictMethod
    @GetMapping("/queryById")
    public Result<SysTenant> queryById(@RequestParam String id) {
        SysTenant sysTenant = service.getById(id);
        if (sysTenant == null) {
            return Result.error("未找到对应实体！");
        }
        return Result.ok(sysTenant);
    }

    /**
     * 查询有效的租户数据
     *
     * @param ids 租户id（多个逗号分隔）
     */
    @GetMapping("/queryList")
    public Result<List<SysTenant>> queryList(@RequestParam(required = false) String ids) {
        LambdaQueryWrapper<SysTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysTenant::getStatus, 1);
        if (StrUtil.isNotEmpty(ids)) {
            query.in(SysTenant::getId, ListUtil.toList(ids.split(",")));
        }
        //此处查询忽略时间条件
        List<SysTenant> list = service.list(query);
        return Result.ok(list);
    }
}
