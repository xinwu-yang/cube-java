package org.cube.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.cube.modules.system.mapper.SysGatewayRouteMapper;
import org.cube.modules.system.service.ISysGatewayRouteService;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.base.BaseMap;
import org.cube.commons.constant.CacheConst;
import org.cube.modules.system.entity.SysGatewayRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class SysGatewayRouteServiceImpl extends ServiceImpl<SysGatewayRouteMapper, SysGatewayRoute> implements ISysGatewayRouteService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addRoute2Redis(String key) {
        List<SysGatewayRoute> ls = this.list(new LambdaQueryWrapper<SysGatewayRoute>().eq(SysGatewayRoute::getStatus, 1));
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(ls));
    }

    @Override
    public void deleteById(String id) {
        this.removeById(id);
        this.refreshRouter();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAll(JsonNode json) {
        try {
            json = json.get("router");
            String id = json.get("id").asText();
            SysGatewayRoute route = getById(id);
            if (ObjectUtil.isEmpty(route)) {
                route = new SysGatewayRoute();
            }
            route.setRouterId(json.get("routerId").asText());
            route.setName(json.get("name").asText());
            route.setPredicates(json.get("predicates").asText());
            String filters = json.get("filters").asText();
            if (ObjectUtil.isEmpty(filters)) {
                filters = "[]";
            }
            route.setFilters(filters);
            route.setUri(json.get("uri").asText());
            if (json.get("status") == null) {
                route.setStatus(1);
            } else {
                route.setStatus(json.get("status").asInt());
            }
            this.saveOrUpdate(route);
        } catch (Exception e) {
            log.error("路由配置解析失败！", e);
        } finally {
            refreshRouter();
        }
    }

    /**
     * 更新redis路由缓存
     */
    private void refreshRouter() {
        // 更新redis路由缓存
        addRoute2Redis(CacheConst.GATEWAY_ROUTES);
        BaseMap params = new BaseMap();
        params.put("handlerName", "refreshRouterListener");
        // 通过redis消息通知网关刷新路由
        redisTemplate.convertAndSend("cube_redis_topic", params);
    }

    @Override
    public void clearRedis() {
        redisTemplate.opsForValue().set(CacheConst.GATEWAY_ROUTES, "");
    }
}
