package org.cube.cloud.starter.job.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cube.xxljob")
public class XxlJobProperties {

    private String adminAddresses;

    private String appname;

    private String ip;

    private int port;

    private String accessToken;

    private String logPath;

    private int logRetentionDays;

    /**
     * 是否开启xxl-job
     */
    private Boolean enable = true;
}
