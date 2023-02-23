package org.cube.codegen.annotations;

import org.cube.codegen.annotations.models.ComponentType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface QueryField {

    /**
     * @return 查询字段
     */
    String value();

    /**
     * @return 显示名称
     */
    String label() default "";

    /**
     * @return 组件
     */
    ComponentType component() default ComponentType.STRING;

    /**
     * @return 数据绑定web组件的数据
     */
    ComponentParam[] componentData() default {};

    /**
     * @return 是否使用字段的组件，value必须和dataIndex一致
     */
    boolean useFieldComponent() default false;
}
