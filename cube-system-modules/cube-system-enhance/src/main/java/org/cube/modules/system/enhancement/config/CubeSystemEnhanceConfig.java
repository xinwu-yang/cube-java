package org.cube.modules.system.enhancement.config;

import org.cube.plugin.sqlexport.SQLExporter;
import org.cube.plugin.sqlexport.mysql.MySQLExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CubeSystemEnhanceConfig {
    /**
     * 导出sql insert 语句
     */
    @Bean
    public SQLExporter sqlExporter() {
        return new MySQLExporter();
    }
}
