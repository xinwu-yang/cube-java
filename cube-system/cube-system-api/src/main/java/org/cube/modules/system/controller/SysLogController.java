package org.cube.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.annotations.DictMethod;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysLog;
import org.cube.modules.system.service.ISysLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 系统日志
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2018-12-26
 */
@Slf4j
@Tag(name = "系统日志相关接口")
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends CubeController<SysLog, ISysLogService> {

    /**
     * 查询日志记录
     *
     * @param syslog   查询参数
     * @param keyWord  关键词查询
     * @param pageNo   页码
     * @param pageSize 每页数据量
     */
    @DictMethod
    @GetMapping("/list")
    public Result<IPage<SysLog>> queryPageList(SysLog syslog, @RequestParam(required = false) String keyWord, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysLog> queryWrapper = QueryGenerator.initQueryWrapper(syslog, req.getParameterMap());
        Page<SysLog> page = new Page<>(pageNo, pageSize);
        if (StrUtil.isNotEmpty(keyWord)) {
            queryWrapper.like("log_content", "%" + keyWord + "%");
        }
        IPage<SysLog> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 删除单个日志记录
     *
     * @param id 主键id
     */
    @AutoLog("删除单个日志记录")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 全部清空日志记录
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @AutoLog("全部清空日志记录")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (StrUtil.isBlank(ids)) {
            return Result.error("参数不识别！");
        }
        if ("allclear".equals(ids)) {
            this.service.removeAll();
        } else {
            this.service.removeByIds(Arrays.asList(ids.split(",")));
        }
        return Result.ok();
    }
}
