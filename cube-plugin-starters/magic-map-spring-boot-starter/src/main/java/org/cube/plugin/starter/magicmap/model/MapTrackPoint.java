package org.cube.plugin.starter.magicmap.model;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 轨迹点
 */
@Data
@NoArgsConstructor
public class MapTrackPoint implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 轨迹id
     */
    private String trackId;

    /**
     * 经度
     */
    private String lat;

    /**
     * 纬度
     */
    private String lng;

    /**
     * 上报时间
     */
    private Date time;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}
