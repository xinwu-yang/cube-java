package org.cube.modules.system.monitor.service.impl;

import cn.hutool.core.util.ObjectUtil;
import org.cube.modules.system.monitor.domain.RedisInfo;
import org.cube.modules.system.monitor.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Redis 监控信息获取
 *
 * @author MrBird
 */
@Slf4j
@Service("redisService")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * Redis详细信息
     */
    @Override
    public List<RedisInfo> getRedisInfo() {
        List<RedisInfo> infoList = new ArrayList<>();
        RedisInfo redisInfo;
        Properties info = redisConnectionFactory.getConnection().info();
        if (info != null) {
            for (Map.Entry<Object, Object> entry : info.entrySet()) {
                redisInfo = new RedisInfo();
                redisInfo.setKey(ObjectUtil.isEmpty(entry.getKey()) ? "" : entry.getKey().toString());
                redisInfo.setValue(ObjectUtil.isEmpty(entry.getValue()) ? "" : entry.getValue().toString());
                infoList.add(redisInfo);
            }
        }
        return infoList;
    }

    @Override
    public Map<String, Object> getKeysSize() {
        Long dbSize = redisConnectionFactory.getConnection().dbSize();
        Map<String, Object> map = new HashMap<>();
        map.put("create_time", System.currentTimeMillis());
        map.put("dbSize", dbSize);
        return map;
    }

    @Override
    public Map<String, Object> getMemoryInfo() {
        Map<String, Object> map = null;
        Properties info = redisConnectionFactory.getConnection().info();
        if (info != null) {
            for (Map.Entry<Object, Object> entry : info.entrySet()) {
                String key = ObjectUtil.isEmpty(entry.getKey()) ? "" : entry.getKey().toString();
                if ("used_memory".equals(key)) {
                    map = new HashMap<>();
                    map.put("used_memory", entry.getValue());
                    map.put("create_time", System.currentTimeMillis());
                }
            }
        }
        return map;
    }
}
