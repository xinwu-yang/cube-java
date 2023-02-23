package org.cube.modules.system.monitor.service;


import org.cube.modules.system.monitor.domain.RedisInfo;
import org.cube.modules.system.monitor.exception.RedisConnectException;

import java.util.List;
import java.util.Map;

public interface RedisService {

    /**
     * 获取 redis 的详细信息
     */
    List<RedisInfo> getRedisInfo() throws RedisConnectException;

    /**
     * 获取 redis key 数量
     */
    Map<String, Object> getKeysSize() throws RedisConnectException;

    /**
     * 获取 redis 内存信息
     */
    Map<String, Object> getMemoryInfo() throws RedisConnectException;
}
