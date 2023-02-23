package org.cube.commons.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 消息队列初始化注解
 *
 * @author zyf
 * @since 2019-07-31 10:43
 **/
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RabbitComponent {

    /**
     * Spring Bean name
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
