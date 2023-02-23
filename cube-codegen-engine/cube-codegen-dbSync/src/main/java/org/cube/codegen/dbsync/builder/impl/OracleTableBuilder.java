package org.cube.codegen.dbsync.builder.impl;

import org.cube.codegen.dbsync.builder.TableSqlBuilder;
import org.cube.codegen.dbsync.models.Attribute;
import org.cube.codegen.dbsync.models.Column;
import org.cube.codegen.dbsync.models.Table;

import java.util.List;

/**
 * 基于Oracle的表结构SQL构造器
 */
public class OracleTableBuilder implements TableSqlBuilder {

    @Override
    public String build(Table table) {
        return null;
    }

    @Override
    public String forcedSyncSql(Table table) {
        return null;
    }

    @Override
    public String addColumn(Table table, Attribute attribute) {
        return null;
    }

    @Override
    public String backupColumn(Table table, Column oldColumn) {
        return null;
    }

    @Override
    public String mappingType(Attribute attribute) {
        return null;
    }

    @Override
    public boolean tableExist(Table table, String schema) {
        return false;
    }

    @Override
    public List<Column> getColumns(Table table) {
        return null;
    }

    @Override
    public String getDataBase(String url) {
        return null;
    }
}
