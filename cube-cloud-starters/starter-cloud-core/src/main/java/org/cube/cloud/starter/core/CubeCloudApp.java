package org.cube.cloud.starter.core;

import org.cube.application.CubeApp;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@CubeApp
@EnableFeignClients(basePackages = "org.cube")
public @interface CubeCloudApp {
}
