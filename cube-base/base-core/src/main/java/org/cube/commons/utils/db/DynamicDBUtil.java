package org.cube.commons.utils.db;

import cn.hutool.core.util.ObjectUtil;
import org.cube.modules.system.model.DynamicDataSourceModel;
import org.cube.commons.utils.ReflectHelper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Spring JDBC 实时数据库访问
 *
 * @author xinwuy
 * @version 2.2.0
 * @since 2021-08-04
 */
@Slf4j
public class DynamicDBUtil {

    /**
     * 获取数据源【最底层方法，不要随便调用】
     */
    private static HikariDataSource getJdbcDataSource(final DynamicDataSourceModel dbSource) {
        String driverClassName = dbSource.getDbDriver();
        String url = dbSource.getDbUrl();
        String dbUser = dbSource.getDbUsername();
        String dbPassword = dbSource.getDbPassword();
        HikariDataSource dataSource = HikariDataSourceBuilder.builder().setJdbcParam(url, dbUser, dbPassword).setName(dbSource.getCode()).setDriverClassName(driverClassName).forMySQL().build();
        log.info("******************************************");
        log.info("*                                        *");
        log.info("*====【" + dbSource.getCode() + "】===== Hikari连接池已启用 ====*");
        log.info("*                                        *");
        log.info("******************************************");
        return dataSource;
    }

    /**
     * 通过 dbKey ,获取数据源
     */
    public static HikariDataSource getDbSourceByDbKey(final String dbKey) {
        //获取多数据源配置
        DynamicDataSourceModel dbSource = DataSourceCachePool.getCacheDynamicDataSourceModel(dbKey);
        //先判断缓存中是否存在数据库链接
        HikariDataSource cacheDbSource = DataSourceCachePool.getCacheBasicDataSource(dbKey);
        if (cacheDbSource != null && !cacheDbSource.isClosed()) {
            return cacheDbSource;
        } else {
            HikariDataSource dataSource = getJdbcDataSource(dbSource);
            DataSourceCachePool.putCacheBasicDataSource(dbKey, dataSource);
            return dataSource;
        }
    }

    /**
     * 关闭数据库连接池
     */
    public static void closeDbKey(final String dbKey) {
        HikariDataSource dataSource = getDbSourceByDbKey(dbKey);
        try {
            if (!dataSource.isClosed()) {
                dataSource.getConnection().commit();
                dataSource.getConnection().close();
                dataSource.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static JdbcTemplate getJdbcTemplate(String dbKey) {
        HikariDataSource dataSource = getDbSourceByDbKey(dbKey);
        return new JdbcTemplate(dataSource);
    }

    /**
     * Executes the SQL statement in this <code>PreparedStatement</code> object,
     * which must be an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     * such as a DDL statement.
     */
    public static int update(final String dbKey, String sql, Object... param) {
        int effectCount;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);
        if (ObjectUtil.isEmpty(param)) {
            effectCount = jdbcTemplate.update(sql);
        } else {
            effectCount = jdbcTemplate.update(sql, param);
        }
        return effectCount;
    }

    public static Object findOne(final String dbKey, String sql, Object... param) {
        List<Map<String, Object>> list;
        list = findList(dbKey, sql, param);
        int size = list.size();
        if (size == 0) {
            log.error("Except one, but not find actually.");
        }
        if (size > 1) {
            log.error("Except one, but more than one actually.");
        }
        return list.get(0);
    }

    /**
     * 直接sql查询 根据clazz返回单个实例
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句
     * @param clazz 返回实例的Class
     * @param param 查询参数
     */
    @SuppressWarnings("unchecked")
    public static <T> Object findOne(final String dbKey, String sql, Class<T> clazz, Object... param) {
        Map<String, Object> map = (Map<String, Object>) findOne(dbKey, sql, param);
        return ReflectHelper.setAll(clazz, map);
    }

    public static List<Map<String, Object>> findList(final String dbKey, String sql, Object... param) {
        List<Map<String, Object>> list;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);

        if (ObjectUtil.isEmpty(param)) {
            list = jdbcTemplate.queryForList(sql);
        } else {
            list = jdbcTemplate.queryForList(sql, param);
        }
        return list;
    }

    //此方法只能返回单列，不能返回实体类
    public static <T> List<T> findList(final String dbKey, String sql, Class<T> clazz, Object... param) {
        List<T> list;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);
        if (ObjectUtil.isEmpty(param)) {
            list = jdbcTemplate.queryForList(sql, clazz);
        } else {
            list = jdbcTemplate.queryForList(sql, clazz, param);
        }
        return list;
    }

    /**
     * 直接sql查询 返回实体类列表
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持 minidao 语法逻辑
     * @param clazz 返回实体类列表的class
     * @param param sql拼接注入中需要的数据
     */
    public static <T> List<T> findListEntities(final String dbKey, String sql, Class<T> clazz, Object... param) {
        List<Map<String, Object>> queryList = findList(dbKey, sql, param);
        return ReflectHelper.transList2Entrys(queryList, clazz);
    }
}
