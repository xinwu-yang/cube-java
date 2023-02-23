package org.cube.plugin.starter.magicmap.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClusterPoint {
    /**
     * 经度
     */
    private String lat;

    /**
     * 纬度
     */
    private String lng;

    /**
     * 名字
     */
    private String name;

    /**
     * 聚合的点
     */
    List<MapPoint> pointList;
}
