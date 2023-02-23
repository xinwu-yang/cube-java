package org.cube.plugin.starter.magicmap.clustering;

import org.cube.plugin.starter.magicmap.clustering.model.LatLng;

import java.util.Collection;

/**
 * 多个点聚合而成的聚合对象
 * @param <T>
 */
public interface ICluster <T extends IClusterItem> {
    /**
     * 聚合后中心点位置
     * @return
     */
    public LatLng getPosition();

    /**
     * 聚合的点列表
     * @return
     */
    Collection<T> getItems();

    /**
     * 聚合的点数量
     * @return
     */
    int getSize();

    String getName();
}
