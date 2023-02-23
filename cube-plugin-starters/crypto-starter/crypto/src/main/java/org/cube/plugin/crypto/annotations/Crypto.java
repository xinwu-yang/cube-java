package org.cube.plugin.crypto.annotations;

import org.cube.plugin.crypto.model.Algorithm;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Crypto {

    // 加密算法
    Algorithm value();
}
