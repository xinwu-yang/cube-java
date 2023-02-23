package org.cube.codegen.annotations;

import java.lang.annotation.*;

/**
 * 一对多注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SubTables {

    /**
     * 一对多类
     */
    Class<?>[] value();
}
