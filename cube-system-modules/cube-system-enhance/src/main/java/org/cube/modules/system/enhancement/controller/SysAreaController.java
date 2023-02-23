package org.cube.modules.system.enhancement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.entity.SysArea;
import org.cube.modules.system.enhancement.service.ISysAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 地区管理
 *
 * @author 杨欣武
 * @version V2.3.5
 * @since 2020-09-10
 */
@Tag(name = "地区管理")
@RestController
@RequestMapping("/sys/area")
public class SysAreaController extends CubeController<SysArea, ISysAreaService> {

    /**
     * 分页列表查询
     *
     * @param sysArea  查询参数
     * @param pageNo   页码
     * @param pageSize 每页数量
     */
    @DictMethod
    @GetMapping("/list")
    @Operation(summary = "分页列表查询")
    public Result<?> queryPageList(SysArea sysArea, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysArea> queryWrapper = QueryGenerator.initQueryWrapper(sysArea, req.getParameterMap());
        Page<SysArea> page = new Page<>(pageNo, pageSize);
        IPage<SysArea> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     *
     * @param sysArea 参数
     */
    @AutoLog("地区管理-新增")
    @PostMapping("/add")
    @Operation(summary = "新增")
    public Result<?> add(@RequestBody SysArea sysArea) {
        return service.addSysArea(sysArea);
    }

    /**
     * 修改
     *
     * @param sysArea 参数
     */
    @AutoLog("地区管理-修改")
    @PutMapping("/edit")
    @Operation(summary = "修改")
    public Result<?> edit(@RequestBody SysArea sysArea) {
        service.updateById(sysArea);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id       主键id
     * @param parentId 父id
     */
    @AutoLog("地区管理-通过id删除")
    @DeleteMapping("/delete")
    @Operation(summary = "通过id删除")
    public Result<?> delete(@RequestParam Long id, @RequestParam Long parentId) {
        return service.deleteSysArea(id, parentId);
    }

    /**
     * 通过id查询
     *
     * @param id 主键id
     */
    @DictMethod
    @GetMapping("/queryById")
    @Operation(summary = "通过id查询")
    public Result<?> queryById(@RequestParam String id) {
        SysArea sysArea = service.getById(id);
        if (sysArea == null) {
            return Result.error("未找到对应数据！");
        }
        return Result.ok(sysArea);
    }
}
