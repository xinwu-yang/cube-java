package org.cube.modules.system.model;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DynamicDataSourceModel {

    /**
     * id
     */
    private String id;

    /**
     * 数据源编码
     */
    private String code;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 驱动类
     */
    private String dbDriver;

    /**
     * 数据源地址
     */
    private String dbUrl;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 用户名
     */
    private String dbUsername;

    /**
     * 密码
     */
    private String dbPassword;

    public DynamicDataSourceModel(Object dbSource) {
        if (dbSource != null) {
            BeanUtil.copyProperties(dbSource, this);
        }
    }
}