package org.cube.plugin.starter.filemanager.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "cube.file-manager")
public class FileServerProperties {
    private Protocol protocol;
    //IP or Host
    private String hostname;
    private int port;
    private String accessKey;
    private String secretKey;
    private boolean isHttps;
    private Map<String, String> params;
}
