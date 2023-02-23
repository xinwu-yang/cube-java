package org.cube.cloud.starter.lock.core.strategy.impl;

import cn.hutool.core.util.StrUtil;
import org.cube.cloud.starter.lock.enums.GlobalConstant;
import org.cube.cloud.starter.lock.prop.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.cube.cloud.starter.lock.core.strategy.RedissonConfigStrategy;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * 主从方式Redisson配置
 * <p>配置方式: 127.0.0.1:6379(主),127.0.0.1:6380(子),127.0.0.1:6381(子)</p>
 *
 * @author zyf
 * @since 2020-11-11
 */
@Slf4j
public class MasterSlaveRedissonConfigStrategyImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String masterNodeAddr = addrTokens[0];
            // 设置主节点ip
            config.useMasterSlaveServers().setMasterAddress(masterNodeAddr);
            if (StrUtil.isNotBlank(password)) {
                config.useMasterSlaveServers().setPassword(password);
            }
            config.useMasterSlaveServers().setDatabase(database);
            // 设置从节点，移除第一个节点，默认第一个为主节点
            List<String> slaveList = new ArrayList<>();
            for (String addrToken : addrTokens) {
                slaveList.add(GlobalConstant.REDIS_CONNECTION_PREFIX + addrToken);
            }
            slaveList.remove(0);
            config.useMasterSlaveServers().addSlaveAddress((String[]) slaveList.toArray());
            log.info("初始化主从方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("主从Redisson初始化错误", e);
            e.printStackTrace();
        }
        return config;
    }
}
