package org.cube.commons.utils.db;

import cn.hutool.extra.spring.SpringUtil;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.constant.CacheConst;
import org.cube.modules.system.model.DynamicDataSourceModel;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源缓存池
 */
public class DataSourceCachePool {
    /**
     * 数据源连接池缓存【本地 class缓存 - 不支持分布式】
     */
    private static final Map<String, HikariDataSource> DB_CACHE = new HashMap<>();
    private static RedisTemplate<String, Object> redisTemplate;

    private static RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringUtil.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 获取多数据源缓存
     */
    public static DynamicDataSourceModel getCacheDynamicDataSourceModel(String dbKey) {
        String redisCacheKey = CacheConst.SYS_DYNAMIC_DB_CACHE + dbKey;
        if (Boolean.TRUE.equals(getRedisTemplate().hasKey(redisCacheKey))) {
            return (DynamicDataSourceModel) getRedisTemplate().opsForValue().get(redisCacheKey);
        }
        CommonAPI commonAPI = SpringUtil.getBean(CommonAPI.class);
        DynamicDataSourceModel dbSource = commonAPI.getDynamicDbSourceByCode(dbKey);
        if (dbSource != null) {
            getRedisTemplate().opsForValue().set(redisCacheKey, dbSource);
        }
        return dbSource;
    }

    public static HikariDataSource getCacheBasicDataSource(String dbKey) {
        return DB_CACHE.get(dbKey);
    }

    /**
     * put 数据源缓存
     */
    public static void putCacheBasicDataSource(String dbKey, HikariDataSource db) {
        DB_CACHE.put(dbKey, db);
    }

    /**
     * 清空数据源缓存
     */
    public static void cleanAllCache() {
        //关闭数据源连接
        for (Map.Entry<String, HikariDataSource> entry : DB_CACHE.entrySet()) {
            String dbKey = entry.getKey();
            HikariDataSource dataSource = entry.getValue();
            if (dataSource != null && dataSource.isRunning()) {
                dataSource.close();
            }
            //清空redis缓存
            getRedisTemplate().delete(CacheConst.SYS_DYNAMIC_DB_CACHE + dbKey);
        }
        //清空缓存
        DB_CACHE.clear();
    }

    public static void removeCache(String dbKey) {
        //关闭数据源连接
        HikariDataSource dataSource = DB_CACHE.get(dbKey);
        if (dataSource != null && dataSource.isRunning()) {
            dataSource.close();
        }
        //清空redis缓存
        getRedisTemplate().delete(CacheConst.SYS_DYNAMIC_DB_CACHE + dbKey);
        //清空缓存
        DB_CACHE.remove(dbKey);
    }
}
