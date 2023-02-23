package org.cube.codegen.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定业务包名
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JavaPackage {

    /**
     * 业务包名
     */
    String businessPackage() default "";

    /**
     * 模块名称
     */
    String modulePackage() default "";
}
