package org.cube.plugin.filemanager;

import org.cube.plugin.filemanager.models.PreSignedDownloadParam;

import java.time.Duration;

/**
 * S3 预签名文件上传下载接口
 *
 * @param <T>
 */
public interface IPreSignedManager<T> extends IClient<T> {
    /**
     * S3 预签名上传文件
     *
     * @param key      文件路径
     * @param bucket   存储桶
     * @param fileSize 文件大小
     * @param duration 签名过期时间
     * @return 上传地址
     */
    String preSignedUpload(String key, String bucket, long fileSize, Duration duration);

    /**
     * S3 预签名下载文件（默认bucket）
     *
     * @param key      文件路径
     * @param duration 签名过期时间
     * @return 下载地址
     */
    String preSignedDownload(String key, Duration duration);

    /**
     * S3 预签名下载文件
     *
     * @param key      文件路径
     * @param bucket   存储桶
     * @param duration 签名过期时间
     * @return 下载地址
     */
    String preSignedDownload(String key, String bucket, Duration duration);

    /**
     * S3 预签名下载文件
     *
     * @param param 下载参数
     * @return 下载地址
     */
    String preSignedDownload(PreSignedDownloadParam param);
}
