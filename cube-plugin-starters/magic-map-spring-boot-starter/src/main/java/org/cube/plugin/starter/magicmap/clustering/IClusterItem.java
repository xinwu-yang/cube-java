package org.cube.plugin.starter.magicmap.clustering;

import org.cube.plugin.starter.magicmap.clustering.model.LatLng;

public interface IClusterItem {
    /**
     * 聚合点的经纬度坐标.
     */
    public LatLng getPosition();

    /**
     * 点Id
     * @return
     */
    String getId();

    public Object getObject();
}
