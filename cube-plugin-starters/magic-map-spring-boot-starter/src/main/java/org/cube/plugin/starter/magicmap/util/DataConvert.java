package org.cube.plugin.starter.magicmap.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.cube.plugin.starter.magicmap.handler.IDataHandler;
import org.cube.plugin.starter.magicmap.model.MapArea;
import org.cube.plugin.starter.magicmap.model.MapPoint;
import org.cube.plugin.starter.magicmap.model.MapTrack;
import org.cube.plugin.starter.magicmap.model.MapTrackPoint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataConvert {

    private static final Map<String, Map<String, Field>> ENTITY_FIELD_MAP = MapUtil.newHashMap(4);

    static {
        // 缓存model下所有的实体类
        ENTITY_FIELD_MAP.put(MapPoint.class.getSimpleName(), ReflectUtil.getFieldMap(MapPoint.class));
        ENTITY_FIELD_MAP.put(MapArea.class.getSimpleName(), ReflectUtil.getFieldMap(MapArea.class));
        ENTITY_FIELD_MAP.put(MapTrack.class.getSimpleName(), ReflectUtil.getFieldMap(MapTrack.class));
        ENTITY_FIELD_MAP.put(MapTrackPoint.class.getSimpleName(), ReflectUtil.getFieldMap(MapTrackPoint.class));
    }

    /**
     * 半结构化数据转换结构化数据
     *
     * @param data 数据转换参数
     * @param <T>  泛型
     * @return 结构化数据列表
     */
    public static <T> List<T> convert(Data<T> data) {
        List<T> returnList = new ArrayList<>();
        for (Map<String, Object> mapData : data.getData()) {
            JSONObject extra = JSONUtil.createObj();
            T t = BeanUtil.toBean(mapData, data.getCls());
            Map<String, Field> fieldMap = ENTITY_FIELD_MAP.get(data.getCls().getSimpleName());
            mapData.forEach((k, v) -> {
                if (!fieldMap.containsKey(k)) {
                    extra.set(k, v);
                }
            });
            ReflectUtil.invoke(t, "setExtra", extra);
            returnList.add(t);
        }
        if (data.getDataHandlers() != null && data.getDataHandlers().size() > 0) {
            for (IDataHandler<T> handler : data.getDataHandlers()) {
                if (StrUtil.isNotEmpty(handler.query())) {
                    if (handler.query().equals(data.getQuery())) {
                        handler.handle(returnList);
                    }
                } else {
                    handler.handle(returnList);
                }
            }
        }
        return returnList;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data<T> {

        /**
         * 要转换的类型
         */
        private Class<T> cls;

        /**
         * 带转换的数据
         */
        private List<Map<String, Object>> data;

        /**
         * 数据后处理
         */
        private List<IDataHandler<T>> dataHandlers;

        /**
         * 指定查询转换
         */
        private String query;
    }
}
