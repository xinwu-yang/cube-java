package org.cube.modules.system.service;

import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.model.api.response.LogInfoResponse;
import org.cube.modules.system.model.api.response.LoginResponse;
import org.cube.modules.system.model.api.response.VisitsInfo;

import java.util.List;

/**
 * 登录相关业务逻辑
 *
 * @author 杨欣武
 * @version 2.5.2
 * @since 2022-08-10
 */
public interface ILoginService {

    /**
     * 整合用户的登录相关信息
     *
     * @param sysUser 用户信息
     * @return 登录信息
     */
    LoginResponse getUserInfo(SysUser sysUser);

    /**
     * 账户密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     * @param checkKey 验证码key
     * @return 用户信息
     */
    LoginResponse loginWithPassword(String username, String password, String captcha, String checkKey);

    /**
     * 手机验证码登录
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 用户信息
     */
    LoginResponse loginWithCode(String phone, String code);

    /**
     * 登出
     */
    void logout();

    /**
     * 今日访问量
     */
    LogInfoResponse visitsToday();

    /**
     * 七日访问量
     */
    List<VisitsInfo> visitsSevenDay();

    /**
     * 更新当前用户登录的部门
     *
     * @param orgCode 当前登录的部门
     * @return 用户信息
     */
    SysUser selectDepart(String orgCode);

    /**
     * 发送短信验证码
     *
     * @param phone 手机号码
     * @param mode  短信验证码模式（登录模式: "0"，注册模式: "1"，安全验证：”2“）
     */
    void sendSms(String phone, String mode);

    /**
     * 随机图像验证码
     *
     * @param key 随机key
     * @return 图像验证码base64
     */
    String randomImage(String key);

    /**
     * 验证码校验
     *
     * @param captcha  验证码
     * @param checkKey 校验key
     */
    void checkCaptcha(String captcha, String checkKey);
}
