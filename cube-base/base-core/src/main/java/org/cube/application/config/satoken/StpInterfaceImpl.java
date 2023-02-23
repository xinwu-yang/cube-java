package org.cube.application.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import org.cube.commons.api.CommonAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SaToken 获取当前登录用户的权限信息和角色信息
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Lazy
    @Autowired
    private CommonAPI commonAPI;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String username = loginId.toString();
        return commonAPI.queryUserAuths(username);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String username = loginId.toString();
        return commonAPI.queryUserRoles(username);
    }
}
