package org.cube.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication(scanBasePackages = "org.cube")
public @interface CubeApp {
}