package org.cube.application.config.properties;

import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "cube.swagger")
public class SwaggerProperties {

    /**
     * Swagger鉴权配置
     */
    private Map<String, SecurityScheme> securitySchemes;

    /**
     * 测试服务器配置
     */
    private List<Server> servers;
}
