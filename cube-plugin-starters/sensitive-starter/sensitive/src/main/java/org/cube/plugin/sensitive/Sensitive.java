package org.cube.plugin.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cube.plugin.sensitive.impl.DefaultSensitiveImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏插件注解
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2021-05-06
 */
@JacksonAnnotationsInside
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = SensitiveSerializer.class)
public @interface Sensitive {

    /**
     * 指定序列化的类型
     *
     * @return 敏感字段类型
     */
    SensitiveType value() default SensitiveType.DEFAULT;

    /**
     * 默认敏感处理：脱敏字符开始位置，下标0开始
     *
     * @return 下标
     */
    int start() default 3;

    /**
     * 默认敏感处理：脱敏字符长度，必须大于0
     *
     * @return 长度
     */
    int length() default 100;

    /**
     * 自定义实现
     *
     * @return 接口的具体实现
     */
    Class<?> custom() default DefaultSensitiveImpl.class;
}

