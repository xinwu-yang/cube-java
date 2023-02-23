package org.cube.cloud.starter.lock.core.strategy;

import org.cube.cloud.starter.lock.prop.RedissonProperties;
import org.redisson.config.Config;

/**
 * Redisson配置构建接口
 *
 * @author zyf
 * @since 2020-11-11
 */
public interface RedissonConfigStrategy {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);
}
