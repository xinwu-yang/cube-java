package org.cube.plugin.starter.magicmap.clustering;

import org.cube.plugin.starter.magicmap.clustering.model.ClusterContext;

import java.util.Collection;
import java.util.Set;

/**
 * 聚合算法接口
 */
public interface IClusterAlgorithm<T extends IClusterItem> {
    /**
     * 增加单个点
     * @param item
     */
    void addItem(T item);

    /**
     * 增加多个点
     * @param items
     */
    void addItems(Collection<T> items);

    /**
     * 清除所有点
     */
    void clearItems();

    /**
     * 删除指定点
     * @param item
     */
    void removeItem(T item);

    /**
     * 执行聚合处理
     * @param ctx
     * @return
     */
    Set<? extends ICluster<T>> getClusters(ClusterContext ctx);
    Set<? extends ICluster<T>> getClustersWithConsumption(ClusterContext ctx);

    /**
     * 获取聚合点
     * @return
     */
    Collection<T> getItems();
}
