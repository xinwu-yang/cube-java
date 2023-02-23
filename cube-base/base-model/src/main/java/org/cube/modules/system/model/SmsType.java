package org.cube.modules.system.model;

/**
 * 验证码类型
 */
public enum SmsType {

    /**
     * 登录验证码
     */
    LOGIN_TEMPLATE,

    /**
     * 忘记密码验证码
     */
    FORGET_PASSWORD_TEMPLATE,

    /**
     * 注册验证码
     */
    REGISTER_TEMPLATE,

    /**
     * 安全验证
     */
    SECURITY_TEMPLATE
}
