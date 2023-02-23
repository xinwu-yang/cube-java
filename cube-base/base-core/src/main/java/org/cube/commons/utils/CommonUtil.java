package org.cube.commons.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.cube.commons.constant.DataBaseConst;
import org.cube.commons.exception.CubeAppException;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Slf4j
public class CommonUtil {

    /**
     * 当前系统数据库类型
     */
    private static String DB_TYPE = "";

    public static String getDatabaseType() {
        if (StrUtil.isNotEmpty(DB_TYPE)) {
            return DB_TYPE;
        }
        DataSource dataSource = SpringUtil.getBean(DataSource.class);
        try {
            return getDatabaseTypeByDataSource(dataSource);
        } catch (SQLException e) {
            //e.printStackTrace();
            log.warn(e.getMessage());
            return "";
        }
    }

    /**
     * 获取数据库类型
     */
    private static String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException {
        if ("".equals(DB_TYPE)) {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if (dbType.contains("mysql")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_MYSQL;
                } else if (dbType.contains("oracle") || dbType.contains("dm")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_ORACLE;
                } else if (dbType.contains("sqlserver") || dbType.contains("sql server")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_SQLSERVER;
                } else if (dbType.contains("postgresql")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_POSTGRESQL;
                } else {
                    throw new CubeAppException("数据库类型:[" + dbType + "]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return DB_TYPE;
    }
}