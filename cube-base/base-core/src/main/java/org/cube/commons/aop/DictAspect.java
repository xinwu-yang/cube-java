package org.cube.commons.aop;

import org.cube.commons.dict.DictComponent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 字典 AOP 类
 * 注入字典显示值
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-10-25
 */
@Slf4j
@Aspect
@Component
public class DictAspect {

    @Autowired
    private DictComponent dictComponent;

    // 定义切点Pointcut
    @Pointcut("@within(org.cube.commons.annotations.DictApi)")
    public void dictApi() {
    }

    @Pointcut("@annotation(org.cube.commons.annotations.DictMethod)")
    public void dictMethod() {
    }

    @Pointcut("dictApi() || dictMethod()")
    public void executeService() {
    }

    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long jsonTimeStart = System.currentTimeMillis();
        Object result = pjp.proceed();
        long jsonTimeEnd = System.currentTimeMillis();
        log.debug("获取JSON数据，耗时：" + (jsonTimeEnd - jsonTimeStart) + "ms");
        long injectJsonStart = System.currentTimeMillis();
        dictComponent.parseDictText(result);
        long injectJsonEnd = System.currentTimeMillis();
        log.debug("注入JSON数据，耗时：" + (injectJsonEnd - injectJsonStart) + "ms");
        return result;
    }
}
