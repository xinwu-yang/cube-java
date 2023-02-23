package org.cube.cloud.starter.redis.client;

import org.cube.cloud.starter.redis.config.RedisConfiguration;
import org.cube.commons.base.BaseMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis api
 *
 * @author 杨欣武
 * @version 2.4.6
 * @apiNote 基于Redis的消息推送服务
 * @since 2022-06-14
 */
@Configuration
public class CubeRedisClient {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发送消息
     */
    public void sendMessage(String handlerName, BaseMap params) {
        params.put("handlerName", handlerName);
        redisTemplate.convertAndSend(RedisConfiguration.DEFAULT_TOPIC, params);
    }

    /**
     * 根据key查询缓存
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }
}
