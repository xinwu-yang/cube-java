package org.cube.plugin.starter.magicmap.model;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地图面
 */
@Data
@NoArgsConstructor
public class MapArea implements Serializable {

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
     * GEOJson数据
     */
    private String geoJson;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}
