package org.cube.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.commons.utils.crypto.SecurityUtil;
import org.cube.commons.utils.db.DataSourceCachePool;
import org.cube.modules.system.entity.SysDataSource;
import org.cube.modules.system.service.ISysDataSourceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 多数据源管理
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-12-25
 */
@Slf4j
@Tag(name = "多数据源管理相关接口")
@Deprecated
@RestController
@RequestMapping("/sys/datasource")
public class SysDataSourceController extends CubeController<SysDataSource, ISysDataSourceService> {

    /**
     * 分页列表查询
     *
     * @param sysDataSource 查询参数
     * @param pageNo        页码
     * @param pageSize      每页数量
     */
    @DictMethod
    @GetMapping("/list")
    public Result<IPage<SysDataSource>> queryPageList(SysDataSource sysDataSource, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysDataSource> queryWrapper = QueryGenerator.initQueryWrapper(sysDataSource, req.getParameterMap());
        Page<SysDataSource> page = new Page<>(pageNo, pageSize);
        IPage<SysDataSource> pageList = service.page(page, queryWrapper);
        List<SysDataSource> records = pageList.getRecords();
        records.forEach(item -> {
            String dbPassword = item.getDbPassword();
            if (StrUtil.isNotBlank(dbPassword)) {
                String decodedStr = SecurityUtil.decrypt(dbPassword);
                item.setDbPassword(decodedStr);
            }
        });
        return Result.ok(pageList);
    }

    /**
     * 查询多数据源
     *
     * @param sysDataSource 查询参数
     */
    @Deprecated
    @GetMapping("/options")
    public Result<?> queryOptions(SysDataSource sysDataSource, HttpServletRequest req) {
        QueryWrapper<SysDataSource> queryWrapper = QueryGenerator.initQueryWrapper(sysDataSource, req.getParameterMap());
        List<SysDataSource> pageList = service.list(queryWrapper);
        JSONArray array = JSONUtil.createArray();
        for (SysDataSource item : pageList) {
            JSONObject option = JSONUtil.createObj();
            option.set("value", item.getCode());
            option.set("label", item.getName());
            option.set("text", item.getName());
            array.add(option);
        }
        return Result.ok(array);
    }

    /**
     * 新增
     *
     * @param sysDataSource 参数
     */
    @AutoLog("多数据源管理-新增")
    @PostMapping("/add")
    public Result<?> add(@RequestBody SysDataSource sysDataSource) {
        String dbPassword = sysDataSource.getDbPassword();
        if (StrUtil.isNotBlank(dbPassword)) {
            String encrypt = SecurityUtil.encrypt(dbPassword);
            sysDataSource.setDbPassword(encrypt);
        }
        service.save(sysDataSource);
        return Result.ok();
    }

    /**
     * 更新
     *
     * @param sysDataSource 参数
     */
    @AutoLog("多数据源管理-更新")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysDataSource sysDataSource) {
        SysDataSource datasource = service.getById(sysDataSource.getId());
        DataSourceCachePool.removeCache(datasource.getCode());
        String dbPassword = sysDataSource.getDbPassword();
        if (StrUtil.isNotBlank(dbPassword)) {
            String encrypt = SecurityUtil.encrypt(dbPassword);
            sysDataSource.setDbPassword(encrypt);
        }
        service.updateById(sysDataSource);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @AutoLog("多数据源管理-通过id删除")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        SysDataSource sysDataSource = service.getById(id);
        DataSourceCachePool.removeCache(sysDataSource.getCode());
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @AutoLog("多数据源管理-批量删除")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        idList.forEach(item -> {
            SysDataSource sysDataSource = service.getById(item);
            DataSourceCachePool.removeCache(sysDataSource.getCode());
        });
        service.removeByIds(idList);
        return Result.ok();
    }

    /**
     * 导出Excel
     */
    @GetMapping("/exportXls")
    public void exportXls(SysDataSource sysDataSource, HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.exportXls(request, response, sysDataSource, "多数据源管理");
    }

    /**
     * 通过Excel导入数据
     */
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        return super.importExcel(request, SysDataSource.class);
    }
}
