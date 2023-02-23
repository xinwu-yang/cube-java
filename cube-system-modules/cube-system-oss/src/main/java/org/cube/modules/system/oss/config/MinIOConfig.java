package org.cube.modules.system.oss.config;

import org.cube.modules.system.oss.utils.MinioUtil;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio文件上传配置文件
 */
@Slf4j
@Configuration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(MinIOProperties.class)
public class MinIOConfig {
    @Autowired
    private MinIOProperties minIOProperties;

    @Bean
    public void initMinio() {
        log.info("Initializing MinIO");
        String minioUrl = minIOProperties.getUrl();
        if (!minioUrl.startsWith("http")) {
            minioUrl = "http://" + minioUrl;
        }
        if (!minioUrl.endsWith("/")) {
            minioUrl = minioUrl.concat("/");
        }
        MinioUtil.setMinioUrl(minioUrl);
        MinioUtil.setMinioName(minIOProperties.getName());
        MinioUtil.setMinioPass(minIOProperties.getPassword());
        MinioUtil.setBucketName(minIOProperties.getBucket());
    }
}
