package org.cube.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * MyBatisPlus 插件开关配置
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/1/13
 */
@Data
@ConfigurationProperties(prefix = "cube.mybatis-plus.plugin")
public class MyBatisPlusPluginProperties {

    /**
     * 是否开启多租户
     */
    private boolean enableTenant;

    /**
     * 是否开启乐观锁
     */
    private boolean enableOptimisticLocker;

    /**
     * 租户表有哪些
     */
    private List<String> tenantTables;
}
