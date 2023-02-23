package org.cube.commons.aop;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.exception.CubeAppException;
import org.cube.commons.utils.spring.RedisUtil;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.annotations.LimitSubmit;
import org.cube.modules.system.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LimitSubmitAspect {

    //封装了redis操作各种方法
    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(org.cube.commons.annotations.LimitSubmit)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object handleSubmit(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取注解信息
        LimitSubmit limitSubmit = method.getAnnotation(LimitSubmit.class);
        int cycle = limitSubmit.cycle();
        String redisKey = limitSubmit.value();
        boolean afterDeleteKey = limitSubmit.afterDeleteKey();
        String key = CacheConst.LIMIT_SUBMIT + getRedisKey(joinPoint, redisKey);
        if (redisUtil.hasKey(key)) {
            String message = limitSubmit.message();
            if ("".equals(message)) {
                message = "请勿重复提交！";
            }
            throw new CubeAppException(message);
        }
        redisUtil.set(key, null, cycle);
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception in {}.{}() with cause = '{}' and exception = '{}'", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
            throw e;
        } finally {
            if (afterDeleteKey) {
                redisUtil.del(redisKey);
            }
        }
    }

    /**
     * 支持多参数，从请求参数进行处理
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint, String key) {
        if (StpUtil.isLogin()) {
            LoginUser sysUser = SystemContextUtil.currentLoginUser();
            if (key.contains("%s")) {
                key = String.format(key, sysUser.getUsername());
            }
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        StringBuilder renderKeyBuilder = new StringBuilder();
        if (parameterNames != null) {
            String[] params = key.split(":");
            for (int i = 0; i < params.length; i++) {
                for (int j = 0; j < parameterNames.length; j++) {
                    String item = parameterNames[j];
                    if (params[i].contains("#" + item) && params[i].contains(".")) { // 渲染对象参数
                        Object obj = joinPoint.getArgs()[j];
                        String expression = key.substring(key.indexOf(".") + 1);
                        String value = BeanUtil.getProperty(obj, expression).toString();
                        renderKeyBuilder.append(value);
                    } else if (params[i].contains("#" + item)) { // 渲染简单参数
                        renderKeyBuilder.append(joinPoint.getArgs()[i].toString());
                    } else { // 不需要渲染
                        renderKeyBuilder.append(params[i]);
                    }
                }
                renderKeyBuilder.append(":");
            }
        }
        return renderKeyBuilder.toString();
    }
}
