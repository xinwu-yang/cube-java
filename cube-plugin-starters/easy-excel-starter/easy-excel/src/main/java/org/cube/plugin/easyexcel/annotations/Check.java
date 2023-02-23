package org.cube.plugin.easyexcel.annotations;

import java.lang.annotation.*;

/**
 * 约束
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {
    /**
     * 用于Java字段和Excel列头对应
     */
    Class[] validated() default {};
    String message() default "参数校验失败";
}
