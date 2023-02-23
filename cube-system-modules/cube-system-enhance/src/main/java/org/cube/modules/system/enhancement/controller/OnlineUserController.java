package org.cube.modules.system.enhancement.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.service.IOnlineUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 在线用户统计
 */
@Tag(name = "在线用户统计")
@RestController
@RequestMapping("/sys/user/online")
public class OnlineUserController {

    @Autowired
    private IOnlineUserService onlineUserService;

    /**
     * 查询在线用户列表
     *
     * @param username 用户名
     */
    @GetMapping("list")
    @Operation(summary = "查询在线用户列表")
    public Result<?> getOnlineUserList(@Parameter(description = "用户名") @RequestParam(required = false) String username) {
        return onlineUserService.getOnlineUserList(username);
    }

    /**
     * 用户心跳
     */
    @GetMapping("heart")
    @Operation(summary = "用户心跳")
    public Result<?> onlineHeart(HttpServletRequest request) {
        String username = StpUtil.getLoginIdAsString();
        return onlineUserService.onlineHeart(username, request);
    }
}
