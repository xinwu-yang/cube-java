package org.cube.plugin.starter.magicmap.handler;

import java.util.List;

/**
 * 数据后处理接口
 *
 * @param <T>
 * @author 杨欣武
 * @since 1.0.1
 */
public interface IDataHandler<T> {

    /**
     * 自定义处理数据后处理
     *
     * @param list 列表数据
     */
    void handle(List<T> list);

    /**
     * 指定查询处理，未指定就是全局
     */
    default String query() {
        return "";
    }
}
