package com.mantoo.mtic.common.utils;

import java.util.Map;

/**
 * @ClassName: mapUtil
 * @Description: Map工具类
 * @Author: renjt
 * @Date: 2019-12-13 10:12
 */
public class MapUtil {
    /**
     * @Description 处理前端数据
     * @Param [map]
     * @Author renjt
     * @Date 2019-12-13 10:13
     */
    public static void initMapData(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if ("undefined".equals(value)) {
                map.put(key, null);
            }
            if ("null".equals(value)) {
                map.put(key, null);
            }
        }
    }
}
