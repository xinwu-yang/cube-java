package org.cube.commons.annotations;

import java.lang.annotation.*;

/**
 * 数据权限注解
 *
 * @author xinwuy
 * @version V2.3.6
 * @since 2022-02-16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PermissionData {

    /**
     * 配置菜单的组件路径，用于数据权限
     */
    String value();
}