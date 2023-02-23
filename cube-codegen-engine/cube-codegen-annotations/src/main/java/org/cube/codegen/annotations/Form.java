package org.cube.codegen.annotations;

import java.lang.annotation.*;

/**
 * 表单参数
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Form {

    /**
     * @return 表名
     */
    String businessPackage() default "";

    /**
     * @return 模块包名
     */
    String entityPackage() default "";

    /**
     * @return 功能模块名
     */
    String description();

    /**
     * @return 实体类名
     */
    String entityName() default "";

    /**
     * @return 是否字段分组
     */
    boolean isGroup() default false;
}
