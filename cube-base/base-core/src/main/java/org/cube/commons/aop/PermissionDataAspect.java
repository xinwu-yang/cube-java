package org.cube.commons.aop;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.utils.DataAuthorizationUtil;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.annotations.PermissionData;
import org.cube.modules.system.model.SysPermissionDataRuleModel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 数据权限切面处理类
 * 当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2019-04-10
 */
@Slf4j
@Aspect
@Component
public class PermissionDataAspect {

    @Autowired
    private CommonAPI commonAPI;

    @Pointcut("@annotation(org.cube.commons.annotations.PermissionData)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = SystemContextUtil.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        PermissionData permissionData = method.getAnnotation(PermissionData.class);
        String component = permissionData.value();
        String requestMethod = request.getMethod();
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        requestPath = filterUrl(requestPath);
        log.debug("拦截请求 >> " + requestPath + ";请求类型 >> " + requestMethod);
        String username = StpUtil.getLoginIdAsString();
        //查询数据权限信息
        List<SysPermissionDataRuleModel> dataRules = commonAPI.queryPermissionDataRule(component, requestPath, username);
        if (dataRules != null && dataRules.size() > 0) {
            DataAuthorizationUtil.installDataSearchCondition(request, dataRules);
        }
        return point.proceed();
    }

    private String filterUrl(String requestPath) {
        String url = "";
        if (StrUtil.isNotEmpty(requestPath)) {
            //url = requestPath.replace("\\", "/");
            url = requestPath.replace("//", "/");
            if (url.contains("//")) {
                url = filterUrl(url);
            }
        }
        return url;
    }
}
