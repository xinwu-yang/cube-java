package org.cube.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.cube.commons.annotations.LimitSubmit;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.model.api.request.LoginRequest;
import org.cube.modules.system.model.api.request.PhoneLoginRequest;
import org.cube.modules.system.model.api.request.SendSMSRequest;
import org.cube.modules.system.model.api.response.LogInfoResponse;
import org.cube.modules.system.model.api.response.LoginResponse;
import org.cube.modules.system.model.api.response.VisitsInfo;
import org.cube.modules.system.service.ILoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 登录API
 *
 * @author 杨欣武
 * @version 2.5.2
 * @since 2022-08-11
 */
@Slf4j
@Tag(name = "登录相关接口")
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    /**
     * 登录
     *
     * @param loginRequest 登录参数
     * @return 用户信息以及凭证
     * @apiNote 账号密码登录
     */
    @PostMapping("/login")
    @Operation(summary = "账号密码登录")
    public Result<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.loginWithPassword(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getCaptcha(), loginRequest.getCheckKey());
        return Result.ok(loginResponse);
    }

    /**
     * 登录
     *
     * @apiNote 手机验证码登录
     */
    @PostMapping("/phoneLogin")
    @Operation(summary = "手机验证码登录")
    public Result<LoginResponse> phoneLogin(@RequestBody @Validated PhoneLoginRequest phoneLoginRequest) {
        LoginResponse loginResponse = loginService.loginWithCode(phoneLoginRequest.getMobile(), phoneLoginRequest.getCaptcha());
        return Result.ok(loginResponse);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<?> logout() {
        loginService.logout();
        return Result.ok();
    }

    /**
     * 获取今日访问量
     */
    @GetMapping("logInfo")
    @Operation(summary = "获取今日访问量")
    public Result<LogInfoResponse> logInfo() {
        LogInfoResponse logInfoResponse = loginService.visitsToday();
        return Result.ok(logInfoResponse);
    }

    /**
     * 获取近7天访问量
     */
    @GetMapping("visitInfo")
    @Operation(summary = "获取今日访问量")
    public Result<List<VisitsInfo>> visitInfo() {
        List<VisitsInfo> visitsInfoList = loginService.visitsSevenDay();
        return Result.ok(visitsInfoList);
    }

    /**
     * 用户选择要登陆的部门
     *
     * @param orgCode 用户登录时选择的orgCode
     * @apiNote 登陆成功后，提示用户选择要登录部门！
     */
    @PutMapping("/selectDepart")
    @Operation(summary = "用户选择要登陆的部门", description = "登陆成功后，提示用户选择要登录部门！")
    // TODO 改造为Get请求，参数为orgCode（query string）,前端也需要修改
    public Result<SysUser> selectDepart(@RequestBody String orgCode) {
        SysUser sysUser = loginService.selectDepart(JSONUtil.parseObj(orgCode).getStr("orgCode"));
        return Result.ok(sysUser);
    }

    /**
     * 验证码发送
     */
    @PostMapping("/sms")
    @Operation(summary = "验证码发送")
    @LimitSubmit(value = "sendSms:#sendSMSRequest.mobile", cycle = 600, message = "验证码10分钟内，仍然有效！")
    public Result<?> sms(@RequestBody SendSMSRequest sendSMSRequest) {
        loginService.sendSms(sendSMSRequest.getMobile(), sendSMSRequest.getSmsmode());
        return Result.ok();
    }

    /**
     * 获取图形验证码
     *
     * @param key 随机Key
     * @return 图片Base64字符串
     */
    @GetMapping("/randomImage/{key}")
    @Operation(summary = "获取图形验证码")
    public Result<String> randomImage(@Parameter(description = "随机key") @PathVariable String key) {
        String base64 = loginService.randomImage(key);
        return Result.ok(base64);
    }

    /**
     * 验证图形验证码
     *
     * @param captcha  验证码
     * @param checkKey 校验key
     */
    @GetMapping("/checkCaptcha")
    @Operation(summary = "验证图形验证码")
    public Result<?> checkCaptcha(@Parameter(description = "验证码") @NotBlank String captcha, @Parameter(description = "验证码Key") @NotBlank String checkKey) {
        if (StrUtil.isEmpty(captcha) || StrUtil.isEmpty(checkKey)) {
            return Result.error("验证码无效！");
        }
        loginService.checkCaptcha(captcha, checkKey);
        return Result.ok();
    }
}