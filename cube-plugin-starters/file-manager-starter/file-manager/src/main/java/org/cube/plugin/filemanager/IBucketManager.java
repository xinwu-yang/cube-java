package org.cube.plugin.filemanager;

/**
 * S3 bucket管理接口
 *
 * @param <T>
 */
public interface IBucketManager<T> extends IClient<T> {
    /**
     * 创建bucket
     *
     * @param name bucket name
     */
    void createBucket(String name);
}
