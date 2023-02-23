package org.cube.codegen.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Groups {

    /**
     * 分组列表
     */
    Group[] value();
}
