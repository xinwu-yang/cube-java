package org.cube.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类描述:  字典注解
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-03-17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

    /**
     * 数据code
     */
    String value();

    /**
     * 数据Text
     */
    String text() default "";

    /**
     * 数据字典表
     */
    String table() default "";
}
