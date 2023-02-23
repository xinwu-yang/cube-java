package org.cube.modules.system.enhancement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 在线用户信息
 */
@Data
@AllArgsConstructor
public class OnlineUser {
    public static final String ONLINE_USER_CACHE = "sys:cache:heartbeat:";

    /**
     * 用户账号
     */
    private String username;

    /**
     * 登录凭证
     */
    private String token;

    /**
     * IP
     */
    private String ip;

    /**
     * 游览器
     */
    private String browse;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private Date loginTime;
}
