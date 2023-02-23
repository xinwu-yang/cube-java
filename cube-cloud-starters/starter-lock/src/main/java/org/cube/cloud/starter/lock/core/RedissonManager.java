package org.cube.cloud.starter.lock.core;

import org.cube.cloud.starter.lock.core.strategy.RedissonConfigStrategy;
import org.cube.cloud.starter.lock.core.strategy.impl.ClusterRedissonConfigStrategyImpl;
import org.cube.cloud.starter.lock.core.strategy.impl.MasterSlaveRedissonConfigStrategyImpl;
import org.cube.cloud.starter.lock.core.strategy.impl.SentinelRedissonConfigStrategyImpl;
import org.cube.cloud.starter.lock.core.strategy.impl.StandaloneRedissonConfigStrategyImpl;
import org.cube.cloud.starter.lock.enums.RedisConnectionType;
import org.cube.cloud.starter.lock.prop.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;

/**
 * Redisson配置管理器，用于初始化的redisson实例
 *
 * @author zyf
 * @since 2020-11-12
 */
@Slf4j
public class RedissonManager {

    private Redisson redisson = null;

    public RedissonManager() {
    }

    public RedissonManager(RedissonProperties redissonProperties) {
        //装配开关
        Boolean enabled = redissonProperties.getEnabled();
        if (enabled) {
            try {
                Config config = RedissonConfigFactory.getInstance().createConfig(redissonProperties);
                redisson = (Redisson) Redisson.create(config);
            } catch (Exception e) {
                log.error("Redisson初始化错误", e);
            }
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    static class RedissonConfigFactory {

        private RedissonConfigFactory() {
        }

        private static volatile RedissonConfigFactory factory = null;

        public static RedissonConfigFactory getInstance() {
            if (factory == null) {
                synchronized (Object.class) {
                    if (factory == null) {
                        factory = new RedissonConfigFactory();
                    }
                }
            }
            return factory;
        }

        /**
         * 根据连接类型創建连接方式的配置
         */
        Config createConfig(RedissonProperties redissonProperties) {
            RedisConnectionType connectionType = redissonProperties.getType();
            // 声明连接方式
            RedissonConfigStrategy redissonConfigStrategy;
            if (connectionType.equals(RedisConnectionType.SENTINEL)) {
                redissonConfigStrategy = new SentinelRedissonConfigStrategyImpl();
            } else if (connectionType.equals(RedisConnectionType.CLUSTER)) {
                redissonConfigStrategy = new ClusterRedissonConfigStrategyImpl();
            } else if (connectionType.equals(RedisConnectionType.MASTERSLAVE)) {
                redissonConfigStrategy = new MasterSlaveRedissonConfigStrategyImpl();
            } else {
                redissonConfigStrategy = new StandaloneRedissonConfigStrategyImpl();
            }
            return redissonConfigStrategy.createRedissonConfig(redissonProperties);
        }
    }
}
