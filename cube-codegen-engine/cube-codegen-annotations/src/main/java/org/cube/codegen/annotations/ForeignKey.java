package org.cube.codegen.annotations;

import org.cube.codegen.annotations.models.RelationType;

import java.lang.annotation.*;

/**
 * 标识和匹配外键
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ForeignKey {

    /**
     * 外键类
     */
    Class<?> value();

    /**
     * 关系类型
     */
    RelationType relationType() default RelationType.ONE_TO_MANY;
}
