package org.cube.plugin.starter.filemanager.spring.boot.autoconfigure;

import org.cube.plugin.filemanager.IBucketManager;
import org.cube.plugin.filemanager.IFileManager;
import org.cube.plugin.filemanager.IPreSignedManager;
import org.cube.plugin.filemanager.IStsManager;
import org.cube.plugin.filemanager.ftp.FTPFileManager;
import org.cube.plugin.filemanager.models.FileServer;
import org.cube.plugin.filemanager.s3.S3BucketManager;
import org.cube.plugin.filemanager.s3.S3FileManager;
import org.cube.plugin.filemanager.s3.S3PreSignedManager;
import org.cube.plugin.filemanager.sts.DefaultStsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Conditional(EnableCondition.class)
@EnableConfigurationProperties(FileServerProperties.class)
public class FileManagerAutoconfiguration {
    private final FileServerProperties fileServerProperties;
    private final FileServer fileServer;

    @Autowired
    public FileManagerAutoconfiguration(FileServerProperties fileServerProperties) {
        this.fileServerProperties = fileServerProperties;
        fileServer = FileServer.builder().build();
        BeanUtils.copyProperties(fileServerProperties, fileServer);
    }

    @Bean(destroyMethod = "close")
    public IFileManager<?> fileManager() {
        Protocol protocol = fileServerProperties.getProtocol();
        log.info("Initializing FileManager with protocol[" + protocol + "]");
        if (protocol == Protocol.FTP) {
            return new FTPFileManager(fileServer);
        }
        return new S3FileManager(fileServer);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnClass(name = "software.amazon.awssdk.services.s3.presigner.S3Presigner")
    public IPreSignedManager<?> preSignedManager() {
        log.info("Initializing S3PreSignedManager");
        return new S3PreSignedManager(fileServer);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnClass(name = "software.amazon.awssdk.services.s3.S3Client")
    public IBucketManager<?> bucketManager() {
        log.info("Initializing S3BucketManager");
        return new S3BucketManager(fileServer);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnClass(name = "software.amazon.awssdk.services.sts.StsClient")
    public IStsManager<?> stsManager() {
        log.info("Initializing BucketManager");
        return new DefaultStsManager(fileServer);
    }
}
