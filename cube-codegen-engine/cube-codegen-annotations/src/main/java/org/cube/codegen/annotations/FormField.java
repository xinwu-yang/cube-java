package org.cube.codegen.annotations;

import org.cube.codegen.annotations.models.ComponentType;

import java.lang.annotation.*;

/**
 * 字段属性
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FormField {

    /**
     * @return 标题
     */
    String title() default "";

    /**
     * @return 数据绑定字段
     */
    String dataIndex() default "";

    /**
     * @return 数据绑定web组件
     */
    ComponentType component() default ComponentType.NONE;

    /**
     * @return 数据绑定web组件的数据
     */
    ComponentParam[] componentParams() default {};

    /**
     * @return 分组Id，需要和@Group搭配使用
     */
    int groupId() default 0;

    /**
     * @return 排序
     */
    int sort() default 0;

    /**
     * @return 是否在列表显示
     */
    boolean showInList() default true;

    /**
     * @return 是否在表单显示
     */
    boolean showInForm() default true;

    /**
     * @return 表单中是否必填
     */
    boolean require() default true;
}
