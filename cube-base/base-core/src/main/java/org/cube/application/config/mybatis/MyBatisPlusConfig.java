package org.cube.application.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.cube.application.config.properties.MyBatisPlusPluginProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * MyBatis-Plus 插件配置
 *
 * @author xinwuy
 */
@Configuration
@MapperScan("org.cube.**.mapper*")
public class MyBatisPlusConfig {

    @Autowired
    private MyBatisPlusPluginProperties myBatisPlusPluginProperties;

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        return paginationInnerInterceptor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cube.mybatis-plus.plugin", name = "enable-tenant", havingValue = "true")
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        //多租户配置 配置后每次执行sql会走一遍他的转化器 如果不需要多租户功能 可以将其注释
        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
        tenantLineInnerInterceptor.setTenantLineHandler(new AppTenantLineHandler(myBatisPlusPluginProperties.getTenantTables()));
        return tenantLineInnerInterceptor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cube.mybatis-plus.plugin", name = "enable-optimistic-locker", havingValue = "true")
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptors) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.setInterceptors(innerInterceptors);
        return mybatisPlusInterceptor;
    }
}
