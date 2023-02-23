package org.cube.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TomcatNative利用APR来提升Tomcat性能的本地API
 *
 * @author xinwuy
 * @version 2.0.0
 * @since 2021-03-04
 */
@Data
@ConfigurationProperties(prefix = "cube.apr")
public class TomcatNativeAprProperties {

    /**
     * 是否启用tomcat-native + apr
     */
    private boolean enableTomcatNative;
}
