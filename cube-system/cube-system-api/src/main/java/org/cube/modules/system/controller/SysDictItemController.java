package org.cube.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysDictItem;
import org.cube.modules.system.service.ISysDictItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 数据字典项
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2018-12-28
 */
@Slf4j
@Tag(name = "数据字典项相关接口")
@RestController
@RequestMapping("/sys/dict/item")
public class SysDictItemController extends CubeController<SysDictItem, ISysDictItemService> {

    /**
     * 分页查询列表
     *
     * @param sysDictItem 查询参数
     * @param pageNo      页码
     * @param pageSize    每页数量
     */
    @DictMethod
    @GetMapping("/list")
    public Result<IPage<SysDictItem>> queryPageList(SysDictItem sysDictItem, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        queryWrapper.orderByAsc("sort_order");
        Page<SysDictItem> page = new Page<>(pageNo, pageSize);
        IPage<SysDictItem> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 新增
     *
     * @param sysDictItem 新增参数
     */
    @AutoLog("新增")
    @PostMapping("/add")
    @CacheEvict(value = CacheConst.SYS_DICT_CACHE, allEntries = true)
    public Result<?> add(@RequestBody SysDictItem sysDictItem) {
        service.save(sysDictItem);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param sysDictItem 编辑参数
     */
    @AutoLog("更新")
    @PutMapping("/edit")
    @CacheEvict(value = CacheConst.SYS_DICT_CACHE, allEntries = true)
    public Result<?> edit(@RequestBody SysDictItem sysDictItem) {
        service.updateById(sysDictItem);
        return Result.ok();
    }

    /**
     * 删除字典项
     *
     * @param id 主键id
     */
    @AutoLog("删除")
    @DeleteMapping("/delete")
    @CacheEvict(value = CacheConst.SYS_DICT_CACHE, allEntries = true)
    public Result<?> delete(@RequestParam String id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除字典数据
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @AutoLog("批量删除")
    @DeleteMapping("/deleteBatch")
    @CacheEvict(value = CacheConst.SYS_DICT_CACHE, allEntries = true)
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.error("参数不识别！");
        }
        service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok();
    }
}
