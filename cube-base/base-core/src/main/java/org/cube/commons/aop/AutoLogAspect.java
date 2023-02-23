package org.cube.commons.aop;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.model.LogDTO;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.enums.OperateLogType;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志，切面处理类
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2021-10-20
 */
@Slf4j
@Aspect
@Component
public class AutoLogAspect {

    @Autowired
    private CommonAPI commonAPI;

    @Pointcut("@annotation(org.cube.commons.annotations.AutoLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        saveSysLog(point, time);
        return result;
    }

    /**
     * 保存系统日志
     */
    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogDTO logDTO = new LogDTO();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);
        String content = autoLog.value();
        // 注解上的描述,操作日志内容
        logDTO.setLogType(autoLog.logType());
        logDTO.setLogContent(content);
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        logDTO.setMethod(className + "." + methodName + "()");
        // 未登录则不记录日志到数据库
        if (!StpUtil.isLogin()) {
            // 已经登录才能自动记录日志
            log.warn("未登录情况下AutoLog不会记录操作日志到数据库，日志类型：{}，方法：{}，日志内容：{}", autoLog.logType(), className + "." + methodName + "()", content);
            return;
        }
        // 设置操作类型
        if (logDTO.getLogType() == CommonConst.OPERATE_LOG) {
            logDTO.setOperateType(getOperateType(methodName, autoLog.operateType()));
        }
        // 获取request
        HttpServletRequest request = SystemContextUtil.getHttpServletRequest();
        // 请求的参数
        logDTO.setRequestParam(getRequestParams(request, joinPoint));
        // 设置IP地址
        logDTO.setIp(HttpServletUtil.getIpAddr(request));
        // 获取登录用户信息
        LoginUser sysUser = SystemContextUtil.currentLoginUser();
        logDTO.setUserid(sysUser.getUsername());
        logDTO.setUsername(sysUser.getRealname());
        logDTO.setCostTime(time);
        logDTO.setCreateTime(new Date());
        commonAPI.addLog(logDTO);
    }

    /**
     * 获取操作类型
     */
    private int getOperateType(String methodName, int operateType) {
        if (operateType > 0) {
            return operateType;
        } else if (methodName.startsWith("list")) {
            return OperateLogType.SELECT.getValue();
        } else if (methodName.startsWith("add")) {
            return OperateLogType.ADD.getValue();
        } else if (methodName.startsWith("edit")) {
            return OperateLogType.UPDATE.getValue();
        } else if (methodName.startsWith("delete")) {
            return OperateLogType.DELETE.getValue();
        } else if (methodName.startsWith("import")) {
            return OperateLogType.IMPORT.getValue();
        } else if (methodName.startsWith("export")) {
            return OperateLogType.EXPORT.getValue();
        } else {
            return OperateLogType.OTHER.getValue();
        }
    }

    /**
     * 获取请求参数
     *
     * @param request:   request
     * @param joinPoint: joinPoint
     * @author scott
     * @since 2020/4/16 0:10
     */
    private String getRequestParams(HttpServletRequest request, JoinPoint joinPoint) {
        String httpMethod = request.getMethod();
        StringBuilder params = new StringBuilder();
        if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
            Object[] paramsArray = joinPoint.getArgs();
            // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
            //  https://my.oschina.net/mengzhang6/blog/2395893
            Object[] arguments = new Object[paramsArray.length];
            for (int i = 0; i < paramsArray.length; i++) {
                if (paramsArray[i] instanceof BindingResult || paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = paramsArray[i];
            }
            //update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
            params = new StringBuilder(JSONUtil.toJsonStr(arguments));
            //update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
                }
            }
        }
        return params.toString();
    }
}
