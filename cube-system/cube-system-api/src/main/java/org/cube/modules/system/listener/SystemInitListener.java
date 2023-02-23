package org.cube.modules.system.listener;

import org.cube.modules.system.service.ISysGatewayRouteService;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.constant.CacheConst;
import org.cube.application.config.condition.CloudModeCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 启动程序，初始化路由配置
 *
 * @author flyme
 * @version 1.1.2
 * @since 2020-11-22
 */
@Slf4j
@Component
@Conditional(CloudModeCondition.class)
public class SystemInitListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Autowired
    private ISysGatewayRouteService sysGatewayRouteService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("服务已启动，初始化路由配置！");
        if (applicationReadyEvent.getApplicationContext().getDisplayName().contains("AnnotationConfigServletWebServerApplicationContext")) {
            sysGatewayRouteService.addRoute2Redis(CacheConst.GATEWAY_ROUTES);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
