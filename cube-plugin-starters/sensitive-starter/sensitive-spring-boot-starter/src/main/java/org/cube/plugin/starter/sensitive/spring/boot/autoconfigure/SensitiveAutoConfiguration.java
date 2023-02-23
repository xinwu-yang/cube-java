package org.cube.plugin.starter.sensitive.spring.boot.autoconfigure;

import org.cube.plugin.sensitive.SensitiveResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 脱敏插件自动配置类
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2021-05-06
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SensitiveProperties.class)
public class SensitiveAutoConfiguration {
    private final SensitiveProperties sensitiveProperties;

    public SensitiveAutoConfiguration(SensitiveProperties sensitiveProperties) {
        this.sensitiveProperties = sensitiveProperties;
    }

    @Bean
    public SensitiveResolver sensitiveResolver() {
        log.info("Initializing SensitiveResolver");
        SensitiveResolver resolver = new SensitiveResolver();
        resolver.enable(sensitiveProperties.isEnable());
        resolver.enablePermissionCheck(sensitiveProperties.isPermissionEnable());
        resolver.whitelist(sensitiveProperties.getWhitelist());
        return resolver;
    }
}

