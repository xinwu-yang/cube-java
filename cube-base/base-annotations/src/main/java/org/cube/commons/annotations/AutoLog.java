package org.cube.commons.annotations;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-04-11
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoLog {

    /**
     * 日志内容
     */
    String value() default "";

    /**
     * 日志类型（1登录日志，2:操作日志）
     */
    int logType() default 2;

    /**
     * 操作日志类型（1查询，2添加，3修改，4删除，5导入，6导出）
     */
    int operateType() default 0;
}
