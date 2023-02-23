package org.cube.cloud.gateway.loader;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import org.cube.cloud.gateway.config.GatewayRoutersConfiguration;
import org.cube.cloud.gateway.config.RouterDataType;
import org.cube.cloud.starter.redis.client.CubeRedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 动态路由加载器
 *
 * @author 杨欣武
 * @version 2.4.2
 * @since 2022-05-12
 */
@Slf4j
@Component
@DependsOn({"gatewayRoutersConfiguration"})
public class DynamicRouteLoader implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    private final DynamicRouteService dynamicRouteService;
    private ConfigService configService;
    private final CubeRedisClient redisClient;

    public DynamicRouteLoader(DynamicRouteService dynamicRouteService, CubeRedisClient redisClient) {
        this.dynamicRouteService = dynamicRouteService;
        this.redisClient = redisClient;
    }

    @PostConstruct
    public void init() {
        String dataType = GatewayRoutersConfiguration.DATA_TYPE;
        log.info("初始化路由规则，路由来源：【{}】", dataType);
        if (RouterDataType.nacos.toString().endsWith(dataType)) {
            loadRoutesByNacos();
        }
        //从数据库加载路由
        if (RouterDataType.database.toString().endsWith(dataType)) {
            loadRoutesByRedis();
        }
    }

    /**
     * 刷新路由
     */
    public Mono<Void> refresh() {
        String dataType = GatewayRoutersConfiguration.DATA_TYPE;
        if (!RouterDataType.yml.toString().endsWith(dataType)) {
            this.init();
            log.info("网关路由已刷新！");
        }
        return Mono.empty();
    }

    /**
     * 从Nacos中读取路由配置
     */
    private void loadRoutesByNacos() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        try {
            String configInfo = configService.getConfig(GatewayRoutersConfiguration.DATA_ID, GatewayRoutersConfiguration.ROUTE_GROUP, GatewayRoutersConfiguration.DEFAULT_TIMEOUT);
            if (StrUtil.isNotBlank(configInfo)) {
                log.info("获取网关当前配置:\r\n{}", configInfo);

                routes = JSONUtil.toList(configInfo, RouteDefinition.class);
            }
        } catch (NacosException e) {
            log.error("初始化网关路由时发生错误", e);
            e.printStackTrace();
        }
        for (RouteDefinition definition : routes) {
            dynamicRouteService.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesResultEvent(this));
        dynamicRouteByNacosListener(GatewayRoutersConfiguration.DATA_ID, GatewayRoutersConfiguration.ROUTE_GROUP);
    }

    /**
     * 从redis中读取路由配置
     */
    private void loadRoutesByRedis() {
        List<RouteDefinition> routes = Lists.newArrayList();
        configService = createConfigService();
        if (configService == null) {
            log.warn("initConfigService fail");
        }
        String configInfo = redisClient.get("gateway_routes");
        if (StrUtil.isNotBlank(configInfo)) {
            log.info("获取网关当前配置:\r\n{}", configInfo);
            JSONArray array = JSONUtil.parseArray(configInfo);
            try {
                routes = getRoutesByJson(array);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        for (RouteDefinition definition : routes) {
            dynamicRouteService.add(definition);
        }
        this.publisher.publishEvent(new RefreshRoutesResultEvent(this));
    }

    /**
     * Redis中的信息需要处理下，转成RouteDefinition对象
     */
    public static List<RouteDefinition> getRoutesByJson(JSONArray array) throws URISyntaxException {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            RouteDefinition route = new RouteDefinition();
            route.setId(obj.getStr("routerId"));
            Object uri = obj.get("uri");
            if (uri == null) {
                route.setUri(new URI("lb://" + obj.getStr("name")));
            } else {
                route.setUri(new URI(obj.getStr("uri")));
            }
            Object predicates = obj.get("predicates");
            if (predicates != null) {
                JSONArray list = JSONUtil.parseArray(predicates.toString());
                List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
                for (Object map : list) {
                    JSONObject json = (JSONObject) map;
                    PredicateDefinition predicateDefinition = new PredicateDefinition();
                    predicateDefinition.setName(json.getStr("name"));
                    JSONArray jsonArray = json.getJSONArray("args");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        predicateDefinition.addArg("_genkey" + j, jsonArray.get(j).toString());
                    }
                    predicateDefinitionList.add(predicateDefinition);
                }
                route.setPredicates(predicateDefinitionList);
            }
            Object filters = obj.get("filters");
            if (filters != null) {
                JSONArray list = JSONUtil.parseArray(filters.toString());
                List<FilterDefinition> filterDefinitionList = new ArrayList<>();
                if (ObjectUtil.isNotEmpty(list)) {
                    for (Object map : list) {
                        JSONObject json = (JSONObject) map;
                        JSONArray jsonArray = json.getJSONArray("args");
                        String name = json.getStr("name");
                        FilterDefinition filterDefinition = new FilterDefinition();
                        for (Object o : jsonArray) {
                            JSONObject params = (JSONObject) o;
                            filterDefinition.addArg(params.getStr("key"), params.get("value").toString());
                        }
                        filterDefinition.setName(name);
                        filterDefinitionList.add(filterDefinition);
                    }
                    route.setFilters(filterDefinitionList);
                }
            }
            routeDefinitions.add(route);
        }
        return routeDefinitions;
    }

    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId 配置id
     * @param group  配置组
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<RouteDefinition> definitionList = JSONUtil.toList(configInfo, RouteDefinition.class);
                    for (RouteDefinition definition : definitionList) {
                        log.info("update route : {}", definition.toString());
                        dynamicRouteService.update(definition);
                    }
                }

                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("从nacos接收动态路由配置出错！", e);
        }
    }

    /**
     * 创建ConfigService
     */
    private ConfigService createConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayRoutersConfiguration.SERVER_ADDR);
            properties.setProperty("namespace", GatewayRoutersConfiguration.NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("创建ConfigService异常", e);
            return null;
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
