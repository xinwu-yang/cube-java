package org.cube.cloud.gateway.config;

import org.cube.cloud.gateway.handler.FallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author scott
 * @since 2020/05/26
 * 路由配置信息
 */
@Slf4j
@Configuration
public class GatewayRoutersConfiguration {

    @Autowired
    private FallbackHandler fallbackHandler;

    public static final long DEFAULT_TIMEOUT = 30000;

    public static String SERVER_ADDR;

    public static String NAMESPACE;

    public static String DATA_ID;

    public static String ROUTE_GROUP;

    /**
     * 路由配置文件数据获取方式[yml,nacos,database]
     */
    public static String DATA_TYPE;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public void setServerAddr(String serverAddr) {
        SERVER_ADDR = serverAddr;
    }

    @Value("${spring.cloud.nacos.discovery.namespace}")
    public void setNamespace(String namespace) {
        NAMESPACE = namespace;
    }

    @Value("${cube.route.config.data-id:#{null}}")
    public void setRouteDataId(String dataId) {
        DATA_ID = dataId + ".json";
    }

    @Value("${cube.route.config.group:DEFAULT_GROUP:#{null}}")
    public void setRouteGroup(String routeGroup) {
        ROUTE_GROUP = routeGroup;
    }

    @Value("${cube.route.config.data-type}")
    public void setDataType(String dataType) {
        DATA_TYPE = dataType;
    }

    @Bean
    public RouterFunction<?> routerFunction() {
        return route(RequestPredicates.path("/fallback").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), fallbackHandler);
    }
}
