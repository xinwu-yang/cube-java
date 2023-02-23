package org.cube.commons.annotations;

import java.lang.annotation.*;

/**
 * 标识需要字典翻译的注解
 * 优化字典翻译性能
 * 仅注解Controller下的方法有效
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-11-04
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictMethod {
}
