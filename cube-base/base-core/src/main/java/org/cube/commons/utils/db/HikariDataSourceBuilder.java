package org.cube.commons.utils.db;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Hikari 数据源构造器
 */
public class HikariDataSourceBuilder {
    private final HikariDataSource hikariDataSource = new HikariDataSource();

    public HikariDataSourceBuilder setJdbcParam(String jdbcUrl, String username, String password) {
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        return this;
    }

    public HikariDataSourceBuilder setName(String name) {
        hikariDataSource.setPoolName(name);
        return this;
    }

    public HikariDataSourceBuilder setDriverClassName(String driverClassName) {
        hikariDataSource.setDriverClassName(driverClassName);
        return this;
    }

    /**
     * MySQL数据库的优化配置
     *
     * @return HikariDataSourceBuilder
     */
    public HikariDataSourceBuilder forMySQL() {
        hikariDataSource.addDataSourceProperty("cachePrepStmts", "true");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource.addDataSourceProperty("useServerPrepStmts", "true");
        hikariDataSource.addDataSourceProperty("useLocalSessionState", "true");
        hikariDataSource.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariDataSource.addDataSourceProperty("cacheResultSetMetadata", "true");
        hikariDataSource.addDataSourceProperty("cacheServerConfiguration", "true");
        hikariDataSource.addDataSourceProperty("elideSetAutoCommits", "true");
        hikariDataSource.addDataSourceProperty("maintainTimeStats", "false");
        return this;
    }

    private HikariDataSourceBuilder() {
    }

    public static HikariDataSourceBuilder builder() {
        return new HikariDataSourceBuilder();
    }

    public HikariDataSource build() {
        return hikariDataSource;
    }
}
