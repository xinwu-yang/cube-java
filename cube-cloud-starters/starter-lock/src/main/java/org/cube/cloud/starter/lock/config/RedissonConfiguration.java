package org.cube.cloud.starter.lock.config;

import lombok.extern.slf4j.Slf4j;
import org.cube.cloud.starter.lock.core.RedissonManager;
import org.cube.cloud.starter.lock.prop.RedissonProperties;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson自动化配置
 *
 * @author zyf
 * @since 2020-11-11
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {

    @Autowired
    private RedissonProperties redissonProperties;

    @Bean
    @ConditionalOnMissingBean
    public RedissonManager redissonManager() {
        log.info("RedissonManager初始化完成，当前连接方式：" + redissonProperties.getType() + "，连接地址：" + redissonProperties.getAddress());
        return new RedissonManager(redissonProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(RedissonManager redissonManager) {
        return redissonManager.getRedisson();
    }
}
