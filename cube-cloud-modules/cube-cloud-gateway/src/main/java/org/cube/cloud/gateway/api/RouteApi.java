package org.cube.cloud.gateway.api;

import org.cube.cloud.gateway.loader.DynamicRouteLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 路由相关API
 *
 * @author 杨欣武
 * @version 2.4.2
 * @since 2022/05/17
 */
@RestController
@RequestMapping("/route")
public class RouteApi {

    @Autowired
    private DynamicRouteLoader dynamicRouteLoader;

    /**
     * 路由手动刷新
     */
    @GetMapping("/refresh")
    public Mono<String> refresh() {
        dynamicRouteLoader.refresh();
        return Mono.just("路由刷新一下！");
    }
}
