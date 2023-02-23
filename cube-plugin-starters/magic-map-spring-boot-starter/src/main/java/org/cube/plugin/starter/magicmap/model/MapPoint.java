package org.cube.plugin.starter.magicmap.model;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地图点
 */
@Data
@NoArgsConstructor
public class MapPoint implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 业务唯一编码，如：设备id、序列号之类的
     */
    private String businessCode;

    /**
     * 经度
     */
    private String lat;

    /**
     * 纬度
     */
    private String lng;

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
     * 点图标
     */
    private String iconUrl;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}
