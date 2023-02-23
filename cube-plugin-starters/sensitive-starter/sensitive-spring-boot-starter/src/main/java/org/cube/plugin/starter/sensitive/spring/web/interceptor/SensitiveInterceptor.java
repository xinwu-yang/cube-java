package org.cube.plugin.starter.sensitive.spring.web.interceptor;

import org.cube.plugin.sensitive.PermissionHandler;
import org.cube.plugin.sensitive.SensitiveResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 脱敏接口拦截，获取当前用户的权限标识
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/1/12
 */
public class SensitiveInterceptor implements HandlerInterceptor {

    @Autowired
    private PermissionHandler permissionHandler;
    @Autowired
    private SensitiveResolver sensitiveResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String currentPermission = permissionHandler.getPermission();
        sensitiveResolver.currentPermission(currentPermission);
        return true;
    }
}
