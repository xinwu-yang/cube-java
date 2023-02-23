package org.cube.plugin.starter.magicmap.model;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 地图轨迹
 */
@Data
@NoArgsConstructor
public class MapTrack implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务唯一编码，如：设备id、序列号之类的
     */
    private String businessCode;

    /**
     * 坐标系类型，如：百度、高德、ArcGis、腾讯、PGis
     */
    private String coordinateType;

    /**
     * 所属地区
     */
    private String areaId;

    /**
     * 内置类型
     */
    private Integer type;

    /**
     * 轨迹点
     */
    List<MapTrackPoint> trackPoints;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}
