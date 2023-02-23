package org.cube.commons.annotations;

import java.lang.annotation.*;

/**
 * 防止重复提交注解
 *
 * @author 杨欣武
 * @version 2.5.x
 * @since 2022-07-12
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitSubmit {

    /**
     * redis键值
     */
    String value();

    /**
     * 重复周期：默认10s
     */
    int cycle() default 10;

    /**
     * 方法调用完后是否删除key
     */
    boolean afterDeleteKey() default false;

    /**
     * 自定义提示信息
     */
    String message() default "";
}
