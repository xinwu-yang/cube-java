package org.cube.codegen.dbsync.builder;

import org.cube.codegen.dbsync.models.Attribute;
import org.cube.codegen.dbsync.models.Column;
import org.cube.codegen.dbsync.models.Table;

import java.util.List;

/**
 * SQL构建器
 */
public interface TableSqlBuilder {
    /**
     * 构建SQL
     *
     * @param table 表信息
     * @return SQL
     */
    String build(Table table);

    /**
     * 生成删除语句
     *
     * @param table 表信息
     * @return 表删除语句
     */
    String forcedSyncSql(Table table);

    /**
     * 生成添加字段语句
     *
     * @param table     表信息
     * @param attribute 字段信息
     * @return Alter SQL
     */
    String addColumn(Table table, Attribute attribute);

    /**
     * 备份旧字段
     *
     * @param table     表信息
     * @param oldColumn 老字段信息
     * @return 修改为备份字段
     */
    String backupColumn(Table table, Column oldColumn);

    /**
     * 降Java数据类型映射到数据库数据类型
     *
     * @param attribute 字段属性
     * @return 类型
     */
    String mappingType(Attribute attribute);

    /**
     * 检查表是否存在
     *
     * @param table  表信息
     * @param schema 数据库名
     * @return 是否存在
     */
    boolean tableExist(Table table, String schema);

    /**
     * 获取表结构
     *
     * @param table 表名
     * @return 表字段列表
     */
    List<Column> getColumns(Table table);

    /**
     * 根据连接字符串获取数据名称
     * @param url 连接字符串
     * @return 数据库名称
     */
    String getDataBase(String url);
}
