package org.cube.plugin.starter.magicmap.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cube.magic-map")
public class MagicMapProperties {

    /**
     * 查询配置文件位置，相对路径从class path读取，绝对路径读取系统目录
     */
    private String queryDir;
}
