package org.cube.codegen.dbsync.builder.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import org.cube.codegen.dbsync.builder.TableSqlBuilder;
import org.cube.codegen.dbsync.models.Attribute;
import org.cube.codegen.dbsync.models.Column;
import org.cube.codegen.dbsync.models.Table;
import org.cube.codegen.annotations.models.DefaultValueType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于MySQL5.7的表结构SQL构造器
 */
public class MySqlTableBuilder implements TableSqlBuilder {

    private static final List<String> NO_LENGTH_TYPE = new ArrayList<>();

    static {
        NO_LENGTH_TYPE.add("text");
        NO_LENGTH_TYPE.add("mediumtext");
        NO_LENGTH_TYPE.add("longtext");
        NO_LENGTH_TYPE.add("blob");
        NO_LENGTH_TYPE.add("mediumblob");
        NO_LENGTH_TYPE.add("longblob");
        NO_LENGTH_TYPE.add("json");
        NO_LENGTH_TYPE.add("double");
    }

    @Override
    public String build(Table table) {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append("`").append(table.getName()).append("`");
        sql.append("(");
        table.getAttributes().forEach(attribute -> {
            // 分析字段类型
            String fieldType = this.mappingType(attribute);
            sql.append("`").append(attribute.getName()).append("`").append(" ").append(fieldType).append(" ");
            if (attribute.isAllowNullValue() && !attribute.getName().equals(table.getPrimaryKey())) {
                sql.append("NULL");
            } else {
                sql.append("NOT NULL");
            }
            sql.append(" ");
            if (!attribute.getDefaultValueType().equals(DefaultValueType.NONE)) {
                sql.append("DEFAULT ");
                if (attribute.getDefaultValueType().equals(DefaultValueType.STR)) {
                    sql.append("'").append(attribute.getDefaultValue()).append("'");
                } else if (attribute.getDefaultValueType().equals(DefaultValueType.NUMBER)) {
                    sql.append(attribute.getDefaultValue());
                }
                sql.append(" ");
            }
            if (!StrUtil.isEmpty(attribute.getComment())) {
                sql.append("COMMENT \"").append(attribute.getComment()).append("\"");
            }
            sql.append(",");
        });
        sql.append("PRIMARY KEY (`").append(table.getPrimaryKey()).append("`)");
        sql.append(")");
        return sql.toString();
    }

    @Override
    public String forcedSyncSql(Table table) {
        return "DROP TABLE IF EXISTS " + table.getName();
    }

    @Override
    public String addColumn(Table table, Attribute attribute) {
        StringBuilder sql = new StringBuilder("ALTER TABLE ");
        sql.append("`").append(table.getName()).append("` ADD ");
        // 分析字段类型
        String fieldType = this.mappingType(attribute);
        sql.append("`").append(attribute.getName()).append("`").append(" ").append(fieldType).append(" ");
        if (attribute.isAllowNullValue()) {
            sql.append("NULL");
        } else {
            sql.append("NOT NULL");
        }
        sql.append(" ");
        if (!StrUtil.isEmpty(attribute.getComment())) {
            sql.append("COMMENT \"").append(attribute.getComment()).append("\"");
        }
        return sql.toString();
    }

    @Override
    public String backupColumn(Table table, Column oldColumn) {
        return "ALTER TABLE `" + table.getName() + "` CHANGE `" + oldColumn.getField() + "`" + " `" + oldColumn.getField() + "_bak` " + oldColumn.getType();
    }

    @Override
    public String mappingType(Attribute attribute) {
        String fieldType;
        // 自定义类型
        if (StrUtil.isNotBlank(attribute.getOverrideType())) {
            // 部分数据类型无需指定长度
            if (NO_LENGTH_TYPE.contains(attribute.getOverrideType().toLowerCase())) {
                fieldType = attribute.getOverrideType();
            } else {
                fieldType = attribute.getOverrideType() + "(" + attribute.getLength() + ")";
            }
            return fieldType;
        }
        // 自适应类型
        String fieldTypeSimpleName = attribute.getType();
        if (fieldTypeSimpleName.contains("ASSIGN_ID") || fieldTypeSimpleName.contains("NONE")) {
            fieldType = "bigint(20)";
        } else if (fieldTypeSimpleName.contains("ASSIGN_UUID")) {
            fieldType = "varchar(32)";
        } else if (fieldTypeSimpleName.contains("int")) {
            if (attribute.getLength() <= 0) {
                attribute.setLength(32);
            }
            fieldType = "int(" + attribute.getLength() + ")";
        } else if (fieldTypeSimpleName.contains("long")) {
            if (attribute.getLength() <= 0) {
                attribute.setLength(20);
            }
            fieldType = "bigint(" + attribute.getLength() + ")";
        } else if (fieldTypeSimpleName.contains("bool")) {
            fieldType = "bit(1)";
        } else if (fieldTypeSimpleName.contains("date")) {
            fieldType = "datetime";
        } else if (fieldTypeSimpleName.contains("double")) {
            fieldType = "double";
        } else if (fieldTypeSimpleName.contains("timestamp")) {
            fieldType = "timestamp";
        } else {
            if (attribute.getLength() <= 0) {
                attribute.setLength(255);
            }
            fieldType = "varchar(" + attribute.getLength() + ")";
        }
        return fieldType;
    }

    @Override
    public boolean tableExist(Table table, String schema) {
        try {
            // 有一丝疑问 查询 COUNT(*) 无论数据库有没有表数值都是1
            Entity entity = Db.use().queryOne("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?", schema, table.getName());
            return entity != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Column> getColumns(Table table) {
        List<Column> columns = null;
        try {
            List<Entity> columnList = Db.use().query("DESC " + table.getName());
            if (columnList.size() > 0) {
                columns = new ArrayList<>();
                List<Column> finalColumns = columns;
                columnList.forEach(entity -> {
                    Column column = new Column();
                    column.setField(entity.getStr("field"));
                    column.setType(entity.getStr("type"));
                    column.setPK("PRI".equals(entity.getStr("key")));
                    column.setAllowNullValue("YES".equals(entity.getStr("null")));
                    finalColumns.add(column);
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    @Override
    public String getDataBase(String url) {
        if (StrUtil.isEmpty(url)) {
            return null;
        }
        String[] urls = url.split("\\?");
        return urls[0].substring(urls[0].lastIndexOf("/") + 1);
    }
}
