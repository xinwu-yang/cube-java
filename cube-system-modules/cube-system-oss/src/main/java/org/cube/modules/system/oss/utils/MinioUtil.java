package org.cube.modules.system.oss.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Date;

/**
 * minio文件上传工具类
 */
@Slf4j
public class MinioUtil {
    private static String minioUrl;
    private static String minioName;
    private static String minioPass;
    private static String bucketName;
    private static MinioClient minioClient;

    public static void setMinioUrl(String minioUrl) {
        MinioUtil.minioUrl = minioUrl;
    }

    public static void setMinioName(String minioName) {
        MinioUtil.minioName = minioName;
    }

    public static void setMinioPass(String minioPass) {
        MinioUtil.minioPass = minioPass;
    }

    public static void setBucketName(String bucketName) {
        MinioUtil.bucketName = bucketName;
    }

    public static String getMinioUrl() {
        return minioUrl;
    }

    public static String getBucketName() {
        return bucketName;
    }

    /**
     * 初始化客户端
     */
    private static void initMinio(String minioUrl, String minioName, String minioPass) {
        if (minioClient == null) {
            minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(minioName, minioPass)
                    .build();
        }
    }

    /**
     * 上传文件
     */
    public static String upload(MultipartFile file, String bizPath, String customBucket) {
        String file_url = "";
        //update-begin-author:wangshuai date:20201012 for: 过滤上传文件夹名特殊字符，防止攻击
        bizPath = StrAttackFilter.filter(bizPath);
        //update-end-author:wangshuai date:20201012 for: 过滤上传文件夹名特殊字符，防止攻击
        String newBucket = bucketName;
        if (StrUtil.isNotEmpty(customBucket)) {
            newBucket = customBucket;
        }
        try {
            initMinio(minioUrl, minioName, minioPass);
            // 检查存储桶是否已经存在
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(newBucket).build())) {
                log.info("Bucket already exists.");
            } else {
                // 创建一个名为ota的存储桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(newBucket).build());
                log.info("create a new bucket.");
            }
            InputStream stream = file.getInputStream();
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            if (StrUtil.isEmpty(originalFilename)) {
                originalFilename = file.getName();
            }
            String fileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + "-" + RandomUtil.randomString(6) + originalFilename.substring(originalFilename.lastIndexOf("."));
            bizPath = StrAttackFilter.filter(bizPath);
            String objectName = bizPath + "/" + fileName;
            // 使用putObject上传一个本地文件到存储桶中。
            if (objectName.startsWith("/")) {
                objectName = objectName.substring(1);
            }
            PutObjectArgs objectArgs = PutObjectArgs.builder().object(objectName)
                    .bucket(newBucket)
                    .contentType("application/octet-stream")
                    .stream(stream, stream.available(), -1).build();
            minioClient.putObject(objectArgs);
            stream.close();
            file_url = minioUrl + newBucket + "/" + objectName;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return file_url;
    }

    /**
     * 文件上传
     */
    public static String upload(MultipartFile file, String bizPath) {
        return upload(file, bizPath, null);
    }

    /**
     * 获取文件流
     */
    public static InputStream getMinioFile(String bucketName, String objectName) {
        InputStream inputStream = null;
        try {
            initMinio(minioUrl, minioName, minioPass);
            GetObjectArgs objectArgs = GetObjectArgs.builder().object(objectName)
                    .bucket(bucketName).build();
            inputStream = minioClient.getObject(objectArgs);
        } catch (Exception e) {
            log.info("文件获取失败" + e.getMessage());
        }
        return inputStream;
    }

    /**
     * 删除文件
     */
    public static void removeObject(String bucketName, String objectName) {
        try {
            initMinio(minioUrl, minioName, minioPass);
            RemoveObjectArgs objectArgs = RemoveObjectArgs.builder().object(objectName)
                    .bucket(bucketName).build();
            minioClient.removeObject(objectArgs);
        } catch (Exception e) {
            log.info("文件删除失败" + e.getMessage());
        }
    }

    /**
     * 获取文件外链
     */
    public static String getObjectURL(String bucketName, String objectName, Integer expires) {
        initMinio(minioUrl, minioName, minioPass);
        try {
            GetPresignedObjectUrlArgs objectArgs = GetPresignedObjectUrlArgs.builder().object(objectName)
                    .bucket(bucketName)
                    .method(Method.GET)
                    .expiry(expires).build();
            String url = minioClient.getPresignedObjectUrl(objectArgs);
            return URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            log.info("文件路径获取失败" + e.getMessage());
        }
        return null;
    }

    /**
     * 上传文件到minio
     */
    public static String upload(InputStream stream, String relativePath) throws Exception {
        initMinio(minioUrl, minioName, minioPass);
        if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            log.info("Bucket already exists.");
        } else {
            // 创建一个名为ota的存储桶
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("create a new bucket.");
        }
        PutObjectArgs objectArgs = PutObjectArgs.builder().object(relativePath)
                .bucket(bucketName)
                .contentType("application/octet-stream")
                .stream(stream, stream.available(), -1).build();
        minioClient.putObject(objectArgs);
        stream.close();
        return minioUrl + bucketName + "/" + relativePath;
    }
}
