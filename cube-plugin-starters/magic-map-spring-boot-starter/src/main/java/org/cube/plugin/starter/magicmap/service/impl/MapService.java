package org.cube.plugin.starter.magicmap.service.impl;

import org.cube.plugin.starter.magicmap.clustering.ICluster;
import org.cube.plugin.starter.magicmap.clustering.IClusterAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.baidu.NonHierarchicalDistanceBasedAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.grid.GridBasedAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.model.ClusterContext;
import org.cube.plugin.starter.magicmap.clustering.model.LatLng;
import org.cube.plugin.starter.magicmap.clustering.model.LatLngClusterItem;
import org.cube.plugin.starter.magicmap.clustering.model.StaticCluster;
import org.cube.plugin.starter.magicmap.clustering.organization.OrganizationAlgorithm;
import org.cube.plugin.starter.magicmap.handler.IDataHandler;
import org.cube.plugin.starter.magicmap.model.*;
import org.cube.plugin.starter.magicmap.modules.SQLResovler;
import org.cube.plugin.starter.magicmap.mybatis.MybatisParser;
import org.cube.plugin.starter.magicmap.mybatis.SqlNode;
import org.cube.plugin.starter.magicmap.service.IMapService;
import org.cube.plugin.starter.magicmap.util.DataConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 地图数据转换服务层
 */
@Service
public class MapService implements IMapService {

    @Autowired
    private SQLResovler sqlResovler;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private List<IDataHandler<MapPoint>> pointHandlers;

    @Autowired(required = false)
    private List<IDataHandler<MapArea>> areaHandlers;

    @Autowired(required = false)
    private List<IDataHandler<MapTrack>> trackHandlers;

    @Autowired(required = false)
    private List<IDataHandler<MapTrackPoint>> trackPointHandlers;

    @Override
    public List<Map<String, Object>> queryData(String query, Map<String, Object> params) {
        String sql = sqlResovler.getSQL(query);
        SqlNode sqlNode = MybatisParser.parse(sql);
        String execSql = sqlNode.getSql(params);
        List<Object> parameters = sqlNode.getParameters();
        List<Map<String, Object>> data;
        if (parameters.size() > 0) {
            data = jdbcTemplate.queryForList(execSql, parameters.toArray());
        } else {
            data = jdbcTemplate.queryForList(execSql);
        }
        return data;
    }

    @Override
    public List<MapPoint> convertPoints(String query, List<Map<String, Object>> data) {
        DataConvert.Data<MapPoint> convertData = new DataConvert.Data<>(MapPoint.class, data, pointHandlers, query);
        return DataConvert.convert(convertData);
    }

    @Override
    public List<ClusterPoint> clusterPoints(String query, List<Map<String, Object>> data, Map<String, Object> paramMap) throws Exception {
        String clusterType = (String) paramMap.get("clusterType");
        IClusterAlgorithm algorithm = null;
        LatLng southWest = null;
        LatLng northEast = null;
        ClusterContext ctx = new ClusterContext();
        if ("grid".equals(clusterType)) {
            String str =  (String) paramMap.get("zoom");
            if (!StringUtils.hasText(str)) {
                throw new Exception("无效的参数:zoom");
            }
            double zoom = Double.valueOf(str);
            ctx.setZoom(zoom);
            algorithm = new GridBasedAlgorithm();
            southWest = new LatLng();
            str = (String)paramMap.get("swLat");
            if (!StringUtils.hasText(str)) {
                throw new Exception("无效的参数:swLat");
            }
            southWest.setLatitude(Double.valueOf(str));
            str = (String)paramMap.get("swLng");
            if (!StringUtils.hasText(str)) {
                throw new Exception("无效的参数:swLng");
            }
            southWest.setLongitude(Double.valueOf(str));

            northEast = new LatLng();
            str = (String)paramMap.get("neLat");
            if (!StringUtils.hasText(str)) {
                throw new Exception("无效的参数:neLat");
            }
            northEast.setLatitude(Double.valueOf(str));
            str = (String)paramMap.get("neLng");
            if (!StringUtils.hasText(str)) {
                throw new Exception("无效的参数:neLng");
            }
            northEast.setLongitude(Double.valueOf(str));

            ctx.setSouthWest(southWest);
            ctx.setNorthEast(northEast);
        } else if ("griddistance".equals(clusterType)) {
            String str =  (String) paramMap.get("zoom");
            if (!StringUtils.hasText(str)) {
                throw new Exception("无效的参数:zoom");
            }
            double zoom = Double.valueOf(str);
            ctx.setZoom(zoom);

            str = (String)paramMap.get("swLat");
            if (StringUtils.hasText(str)) {
                if (null == southWest) {
                    southWest = new LatLng();
                }
                southWest.setLatitude(Double.valueOf(str));
            }

            str = (String)paramMap.get("swLng");
            if (StringUtils.hasText(str)) {
                if (null == southWest) {
                    southWest = new LatLng();
                }
                southWest.setLongitude(Double.valueOf(str));
            }
            if ((null != southWest) && ((null == southWest.getLatitude()) || (null == southWest.getLongitude()))) {
                throw new Exception("swLat与swLng必须同时指定");
            }

            str = (String)paramMap.get("neLat");
            if (StringUtils.hasText(str)) {
                if (null == northEast) {
                    northEast = new LatLng();
                }
                northEast.setLatitude(Double.valueOf(str));
            }

            str = (String)paramMap.get("neLng");
            if (StringUtils.hasText(str)) {
                if (null == northEast) {
                    northEast = new LatLng();
                }
                northEast.setLongitude(Double.valueOf(str));
            }
            if ((null != northEast) && ((null == northEast.getLatitude()) || (null == northEast.getLongitude()))) {
                throw new Exception("neLat与neLng必须同时指定");
            }

            ctx.setSouthWest(southWest);
            ctx.setNorthEast(northEast);

            algorithm = new NonHierarchicalDistanceBasedAlgorithm(southWest, northEast);
        } else if ("organization".equals(clusterType)) {
            String[] fields= (String[])paramMap.get("clusterFields");
            if (ObjectUtils.isEmpty(fields)) {
                throw new Exception("无效的参数:clusterFields");
            }

            List<String> clusterFields = Arrays.asList(fields);
            algorithm = new OrganizationAlgorithm();
            ctx.setClusterFields(clusterFields);
        }

        List<ClusterPoint> clusterPoints = new ArrayList<>();
        DataConvert.Data<MapPoint> convertData = new DataConvert.Data<>(MapPoint.class, data, pointHandlers, query);
        List<MapPoint> pointList = DataConvert.convert(convertData);
        if (!ObjectUtils.isEmpty(pointList)) {
            for (MapPoint mapPoint : pointList) {
                LatLngClusterItem clusterItem = new LatLngClusterItem(mapPoint.getId(), Double.valueOf(mapPoint.getLat()),
                        Double.valueOf(mapPoint.getLng()));
                clusterItem.setObject(mapPoint);
                algorithm.addItem(clusterItem);
            }

            Set<ICluster> staticClusters = algorithm.getClusters(ctx);
            for (ICluster<LatLngClusterItem> staticCluster : staticClusters) {
                ClusterPoint clusterPoint = new ClusterPoint();
                if (staticCluster instanceof StaticCluster) {
                    clusterPoint.setLat(String.valueOf(staticCluster.getPosition().getLatitude()));
                    clusterPoint.setLng(String.valueOf(staticCluster.getPosition().getLongitude()));
                    clusterPoint.setPointList(new ArrayList<>());
                    clusterPoint.setName(staticCluster.getName());
                    for (LatLngClusterItem item : staticCluster.getItems()) {
                        clusterPoint.getPointList().add((MapPoint) item.getObject());
                    }
                } else if (staticCluster instanceof NonHierarchicalDistanceBasedAlgorithm.QuadItem) {
                    NonHierarchicalDistanceBasedAlgorithm.QuadItem cluster = (NonHierarchicalDistanceBasedAlgorithm.QuadItem) staticCluster;
                    clusterPoint.setLat(String.valueOf(cluster.getPosition().getLatitude()));
                    clusterPoint.setLng(String.valueOf(cluster.getPosition().getLongitude()));
                    clusterPoint.setPointList(new ArrayList<>());
                    clusterPoint.setName(cluster.getName());
                    clusterPoint.getPointList().addAll(cluster.getItems());
                }

                clusterPoints.add(clusterPoint);
            }
        }

        return clusterPoints;
    }

    @Override
    public List<MapArea> convertAreas(String query, List<Map<String, Object>> data) {
        DataConvert.Data<MapArea> convertData = new DataConvert.Data<>(MapArea.class, data, areaHandlers, query);
        return DataConvert.convert(convertData);
    }

    @Override
    public List<MapTrack> convertTracks(String query, List<Map<String, Object>> data) {
        DataConvert.Data<MapTrack> convertData = new DataConvert.Data<>(MapTrack.class, data, trackHandlers, query);
        return DataConvert.convert(convertData);
    }

    @Override
    public List<MapTrackPoint> convertTrackPoints(String query, List<Map<String, Object>> data) {
        DataConvert.Data<MapTrackPoint> convertData = new DataConvert.Data<>(MapTrackPoint.class, data, trackPointHandlers, query);
        return DataConvert.convert(convertData);
    }
}
