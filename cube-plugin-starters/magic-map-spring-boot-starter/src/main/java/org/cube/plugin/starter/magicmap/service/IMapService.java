package org.cube.plugin.starter.magicmap.service;

import org.cube.plugin.starter.magicmap.model.*;

import java.util.List;
import java.util.Map;

public interface IMapService {

    /**
     * 根据名称查询出数据
     *
     * @param query 查询名称
     * @return 数据
     */
    List<Map<String, Object>> queryData(String query, Map<String, Object> params);

    /**
     * 将SQL查询出来的数据封装成MapPoint
     *
     * @return 资源点列表
     */
    List<MapPoint> convertPoints(String query, List<Map<String, Object>> data);

    List<ClusterPoint> clusterPoints(String query, List<Map<String, Object>> data, Map<String, Object> paramMap) throws Exception;

    /**
     * 将SQL查询出来的数据封装成MapPoint
     *
     * @return 资源点列表
     */
    List<MapArea> convertAreas(String query, List<Map<String, Object>> data);

    /**
     * 将SQL查询出来的数据封装成MapTrack
     *
     * @return 资源点列表
     */
    List<MapTrack> convertTracks(String query, List<Map<String, Object>> data);

    /**
     * 将SQL查询出来的数据封装成MapTrackPoint
     *
     * @return 资源点列表
     */
    List<MapTrackPoint> convertTrackPoints(String query, List<Map<String, Object>> data);
}
