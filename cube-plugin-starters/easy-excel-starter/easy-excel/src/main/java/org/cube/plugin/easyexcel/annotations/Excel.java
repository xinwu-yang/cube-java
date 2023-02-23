package org.cube.plugin.easyexcel.annotations;

import java.lang.annotation.*;

/**
 * 匹配Excel导出字段
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /**
     * 用于Java字段和Excel列头对应
     */
    String value() default "";
}
