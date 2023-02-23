package org.cube.codegen.annotations;

import java.lang.annotation.*;

/**
 * 组件参数
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ComponentParam {

    /**
     * 组件参数key
     */
    String key() default "";

    /**
     * 组件参数value
     */
    String value() default "";
}
