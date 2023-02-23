package org.cube.plugin.filemanager;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

/**
 * 文件管理接口
 *
 * @param <T>
 */
public interface IFileManager<T> extends IClient<T> {
    /**
     * 上传文件
     *
     * @param key         文件路径
     * @param file        文件
     * @param otherParams 其他参数
     */
    void upload(String key, File file, Map<String, String> otherParams);

    /**
     * 上传文件
     *
     * @param key  文件路径
     * @param file 文件
     */
    void upload(String key, File file);

    /**
     * 上传文件
     *
     * @param key         文件路径
     * @param bytes       文件二进制
     * @param otherParams 其他参数
     */
    void upload(String key, byte[] bytes, Map<String, String> otherParams);

    /**
     * 上传文件
     *
     * @param key   文件路径
     * @param bytes 文件二进制
     */
    void upload(String key, byte[] bytes);

    /**
     * 下载文件
     *
     * @param key          文件路径
     * @param outputStream 输出流
     * @param otherParams  其他参数
     */
    void download(String key, OutputStream outputStream, Map<String, String> otherParams);
}
