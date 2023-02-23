package org.cube.cloud.gateway.handler;

import org.cube.commons.base.BaseMap;
import org.cube.cloud.starter.redis.listener.CubeRedisListener;
import lombok.extern.slf4j.Slf4j;
import org.cube.cloud.gateway.loader.DynamicRouteLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 路由刷新监听
 */
@Slf4j
@Component
public class RefreshRouterListener implements CubeRedisListener {

    @Resource
    private DynamicRouteLoader dynamicRouteLoader;

    @Override
    public void onMessage(BaseMap message) {
        dynamicRouteLoader.refresh();
    }
}