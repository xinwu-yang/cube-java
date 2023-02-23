package org.cube.codegen.annotations.db;

import java.lang.annotation.*;

/**
 * 数据库字段配置
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface DBField {

    /**
     * 重写数据类型
     */
    String type() default "";

    /**
     * 重写数据长度
     */
    int length() default 0;

    /**
     * 是否允许为空
     */
    boolean allowNullValue() default true;
}
