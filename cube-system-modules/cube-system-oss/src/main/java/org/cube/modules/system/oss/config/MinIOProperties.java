package org.cube.modules.system.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cube.minio")
public class MinIOProperties {
    private String url;
    private String name;
    private String password;
    private String bucket;
}
