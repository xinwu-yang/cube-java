package org.cube.cloud.starter.redis.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis配置
 *
 * @author pangu
 */
@Getter
@Setter
@ConfigurationProperties(CubeRedisProperties.PREFIX)
public class CubeRedisProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "spring.redis";

    /**
     * 是否开启Lettuce
     */
    private Boolean enable = true;
}
