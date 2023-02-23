package org.cube.plugin.filemanager;

/**
 * 客户端接口
 *
 * @param <T>
 */
public interface IClient<T> extends AutoCloseable {
    /**
     * 获取底层API Client
     *
     * @return T
     */
    T defaultClient();
}
