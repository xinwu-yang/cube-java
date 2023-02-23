package org.cube.application.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2021/12/22
 */
@Data
@ConfigurationProperties(prefix = "cube.resources")
public class ResourcesProperties {

    /**
     * 上传路径
     */
    private String uploadPath;

    /**
     * webapp路径
     */
    private String webapp;
}
