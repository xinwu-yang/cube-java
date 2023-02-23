package org.cube.codegen.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Group {

    /**
     * 序号
     */
    int id();

    /**
     * 名称
     */
    String name();
}
