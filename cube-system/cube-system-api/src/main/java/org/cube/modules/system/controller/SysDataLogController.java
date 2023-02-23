package org.cube.modules.system.controller;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.system.api.ISysBaseAPI;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysDataLog;
import org.cube.modules.system.service.ISysDataLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 数据日志
 */
@Slf4j
@Tag(name = "数据日志相关接口")
@RestController
@RequestMapping("/sys/datalog")
public class SysDataLogController extends CubeController<SysDataLog, ISysDataLogService> {

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param dataLog  查询参数
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @param req      请求
     * @return 数据日志列表
     */
    @GetMapping("/list")
    public Result<IPage<SysDataLog>> queryPageList(SysDataLog dataLog, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysDataLog> queryWrapper = QueryGenerator.initQueryWrapper(dataLog, req.getParameterMap());
        Page<SysDataLog> page = new Page<>(pageNo, pageSize);
        IPage<SysDataLog> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 查询对比数据
     */
    @GetMapping("/queryCompareList")
    public Result<List<SysDataLog>> queryCompareList(@RequestParam String dataId1, @RequestParam String dataId2) {
        List<String> idList = ListUtil.of(dataId1, dataId2);
        List<SysDataLog> list = service.listByIds(idList);
        return Result.ok(list);
    }

    /**
     * 查询版本信息
     */
    @GetMapping("/queryDataVerList")
    public Result<List<SysDataLog>> queryDataVerList(@RequestParam String dataTable, @RequestParam String dataId) {
        QueryWrapper<SysDataLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_table", dataTable);
        queryWrapper.eq("data_id", dataId);
        List<SysDataLog> list = service.list(queryWrapper);
        return Result.ok(list);
    }
}
