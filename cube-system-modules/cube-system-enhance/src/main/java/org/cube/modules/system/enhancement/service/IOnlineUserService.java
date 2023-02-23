package org.cube.modules.system.enhancement.service;


import org.cube.commons.base.Result;

import javax.servlet.http.HttpServletRequest;

public interface IOnlineUserService {
    /**
     * 查询在线用户列表
     *
     * @return 查询在线用户列表
     */
    Result<?> getOnlineUserList(String username);

    /**
     * 用户在线心跳
     */
    Result<?> onlineHeart(String id, HttpServletRequest request);
}
