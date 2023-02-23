package org.cube.plugin.starter.magicmap.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Map;

public class RequestParamUtil {

    /**
     * 通过QueryString得到Map参数
     *
     * @param queryStr 查询字符串
     * @return Map对象
     */
    public static Map<String, Object> getStrObjMap(String queryStr) {
        String[] paramsArray = queryStr.split("&");
        Map<String, Object> paramsMap = MapUtil.newHashMap(paramsArray.length);
        for (String kv : paramsArray) {
            String[] param = kv.split("=");
            String key = param[0];
            String val = param[1];
            if (StrUtil.isNotEmpty(val) && val.contains(",")) {
                String[] arrayValue = val.split(",");
                paramsMap.put(key, arrayValue);
            } else {
                paramsMap.put(key, val);
            }
        }
        return paramsMap;
    }
}
