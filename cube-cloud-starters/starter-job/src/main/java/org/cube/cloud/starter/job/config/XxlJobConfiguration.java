package org.cube.cloud.starter.job.config;

import org.cube.cloud.starter.job.prop.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务配置
 *
 * @author 杨欣武
 * @version 2.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(value = "cube.xxljob.enabled", havingValue = "true", matchIfMissing = true)
public class XxlJobConfiguration {

    @Autowired
    private XxlJobProperties xxlJobProperties;

    @Bean
    @ConditionalOnClass
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info("XXL-JOB Config Init");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppname());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }
}
