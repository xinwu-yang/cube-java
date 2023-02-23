package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.JsonNode;
import org.cube.modules.system.entity.SysGatewayRoute;

/**
 * Gateway路由管理
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysGatewayRouteService extends IService<SysGatewayRoute> {

    /**
     * 添加所有的路由信息到redis
     */
    void addRoute2Redis(String key);

    /**
     * 删除路由
     */
    void deleteById(String id);

    /**
     * 保存路由配置
     */
    void updateAll(JsonNode array);

    /**
     * 清空redis中的route信息
     */
    void clearRedis();
}
