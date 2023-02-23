package org.cube.plugin.starter.magicmap.spring.boot.autoconfigure;

import org.cube.plugin.starter.magicmap.modules.SQLResovler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(MagicMapProperties.class)
public class MagicMapAutoConfiguration {

    private final MagicMapProperties magicMapProperties;

    @Autowired
    public MagicMapAutoConfiguration(MagicMapProperties magicMapProperties) {
        this.magicMapProperties = magicMapProperties;
    }

    @Bean
    public SQLResovler sqlResovler() {
        SQLResovler sqlResovler = new SQLResovler();
        sqlResovler.loadQueries(magicMapProperties.getQueryDir());
        return sqlResovler;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
