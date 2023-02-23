package org.cube.codegen.annotations.db;

import org.cube.codegen.annotations.models.DefaultValueType;

import java.lang.annotation.*;

/**
 * 数据库字段默认值配置
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface DefaultValue {

    /**
     * 默认值
     */
    String value() default "";

    /**
     * 默认值类型
     */
    DefaultValueType type() default DefaultValueType.NONE;
}
