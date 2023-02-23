package org.cube.commons.annotations;

import java.lang.annotation.*;

/**
 * 标识需要字典翻译的注解
 * 优化字典翻译性能
 * 仅注解Controller有效
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-10-25
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictApi {
}
