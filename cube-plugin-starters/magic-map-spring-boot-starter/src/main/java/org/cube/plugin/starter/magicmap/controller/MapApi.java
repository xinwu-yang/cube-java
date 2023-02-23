package org.cube.plugin.starter.magicmap.controller;

import cn.hutool.core.util.StrUtil;
import org.cube.commons.base.Result;
import org.cube.plugin.starter.magicmap.model.MapTrack;
import org.cube.plugin.starter.magicmap.model.MapTrackPoint;
import org.cube.plugin.starter.magicmap.service.IMapService;
import org.cube.plugin.starter.magicmap.util.RequestParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("map-api")
public class MapApi {

    @Autowired
    private IMapService mapService;

    /**
     * 地图通用API
     */
    @GetMapping("{type}/search")
    public Result<List<?>> pointSearch(@PathVariable String type, @RequestParam String query, @RequestParam(required = false) String subQuery, HttpServletRequest request) throws Exception {
        String queryStr = request.getQueryString();
        Map<String, Object> paramsMap = RequestParamUtil.getStrObjMap(queryStr);
        Enumeration<String> attrNames = request.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attr = attrNames.nextElement();
            paramsMap.put(attr, request.getAttribute(attr));
        }
        List<Map<String, Object>> data = mapService.queryData(query, paramsMap);
        if ("point".equals(type)) {
            return Result.ok(mapService.convertPoints(query, data));
        } else if ("cluster".equals(type)) {
            return Result.ok(mapService.clusterPoints(query, data, paramsMap));
        } else if ("area".equals(type)) {
            return Result.ok(mapService.convertAreas(query, data));
        } else if ("track".equals(type)) {
            List<MapTrack> tracks = mapService.convertTracks(query, data);
            if (StrUtil.isNotEmpty(subQuery)) {
                for (MapTrack track : tracks) {
                    String trackId = track.getId();
                    paramsMap.put("trackId", trackId);
                    List<Map<String, Object>> trackPointData = mapService.queryData(subQuery, paramsMap);
                    List<MapTrackPoint> trackPoints = mapService.convertTrackPoints(subQuery, trackPointData);
                    track.setTrackPoints(trackPoints);
                }
            }
            return Result.ok(tracks);
        }
        throw new IllegalArgumentException("没有这个类型的接口！");
    }
}
