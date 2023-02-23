package org.cube.modules.system.enhancement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.base.CubeController;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.entity.SysPermissionHistory;
import org.cube.modules.system.enhancement.model.PermissionClickCount;
import org.cube.modules.system.enhancement.model.SysPermissionHistoryInfo;
import org.cube.modules.system.enhancement.service.ISysPermissionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单点击记录
 */
@Slf4j
@Tag(name = "菜单点击记录")
@RestController
@RequestMapping("/sys/permission/history")
public class SysPermissionHistoryController extends CubeController<SysPermissionHistory, ISysPermissionHistoryService> {

    /**
     * 收集用户点击菜单记录
     *
     * @param fullPath 菜单路径
     */
    @GetMapping("collect")
    @Operation(summary = "收集用户点击菜单记录")
    public Result<?> collect(@Parameter(description = "菜单路径") @RequestParam String fullPath) {
        return service.collect(fullPath);
    }

    /**
     * 菜单点击次数统计列表
     *
     * @param sysPermissionHistoryInfo 查询条件
     */
    @PostMapping("clickCount")
    @Operation(summary = "菜单点击次数统计列表")
    public Result<?> permissionClickCountList(@RequestBody SysPermissionHistoryInfo sysPermissionHistoryInfo) {
        Page<PermissionClickCount> page = new Page<>(sysPermissionHistoryInfo.getPageNo(), sysPermissionHistoryInfo.getPageSize());
        return service.permissionClickCount(sysPermissionHistoryInfo, page);
    }
}
