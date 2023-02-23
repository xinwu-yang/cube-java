package org.cube.modules.system.oss.config;

import org.cube.modules.system.oss.utils.OssBootUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云存储 配置
 */
@Configuration
public class OssConfiguration {

    @Value("${cube.oss.endpoint}")
    private String endpoint;
    @Value("${cube.oss.accessKey}")
    private String accessKeyId;
    @Value("${cube.oss.secretKey}")
    private String accessKeySecret;
    @Value("${cube.oss.bucketName}")
    private String bucketName;
    @Value("${cube.oss.staticDomain}")
    private String staticDomain;

    @Bean
    public void initOssBootConfiguration() {
        OssBootUtil.setEndPoint(endpoint);
        OssBootUtil.setAccessKeyId(accessKeyId);
        OssBootUtil.setAccessKeySecret(accessKeySecret);
        OssBootUtil.setBucketName(bucketName);
        OssBootUtil.setStaticDomain(staticDomain);
    }
}