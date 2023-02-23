package org.cube.codegen.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface QueryFields {

    /**
     * @return 查询条件数组
     */
    QueryField[] value();
}
