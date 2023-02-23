package org.cube.codegen.dbsync;

import cn.hutool.db.Db;
import cn.hutool.setting.dialect.Props;
import org.cube.codegen.dbsync.builder.TableSqlBuilder;
import org.cube.codegen.dbsync.builder.impl.MySqlTableBuilder;
import org.cube.codegen.dbsync.builder.impl.OracleTableBuilder;
import org.cube.codegen.dbsync.models.Attribute;
import org.cube.codegen.dbsync.models.Column;
import org.cube.codegen.dbsync.models.Table;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DBSync {
    private static TableSqlBuilder tableSqlBuilder;

    // 初始化
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            tableSqlBuilder = new MySqlTableBuilder();
        } catch (ClassNotFoundException mysqlDriver) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                tableSqlBuilder = new MySqlTableBuilder();
            } catch (ClassNotFoundException mysqlOldDriver) {
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    tableSqlBuilder = new OracleTableBuilder();
                } catch (ClassNotFoundException oracleDriver) {
                    log.error("找不到MySQL/Oracle驱动!");
                }
            }
        }
    }

    /**
     * 同步Entity到数据库
     *
     * @param entities 字节码对象集合
     * @throws SQLException sql执行错误
     */
    public static void sync(List<Class<?>> entities) throws SQLException {
        Props props = new Props("db.setting");
        props.autoLoad(true);
        List<String> sqlList = new ArrayList<>();
        for (Class<?> cls : entities) {
            Table table = ClassAnalyst.analyze(cls);
            String sql = tableSqlBuilder.build(table);
            // 检查表是否已经存在
            if (tableSqlBuilder.tableExist(table, tableSqlBuilder.getDataBase(props.getProperty("url")))) {
                // 强制同步(先删除原表)
                if (table.isForcedSync()) {
                    log.info("Forced Sync Table: {}", table.getName());
                    sqlList.add(tableSqlBuilder.forcedSyncSql(table));
                    sqlList.add(sql);
                } else {
                    // 判断字段是否已经在表中
                    List<Column> columns = tableSqlBuilder.getColumns(table);
                    for (Attribute attribute : table.getAttributes()) {
                        String name = attribute.getName();
                        int size = columns.size();
                        for (int i = 0; i < size; i++) {
                            Column column = columns.get(i);
                            if (name.equals(column.getField())) { // 找到该字段
                                String type = tableSqlBuilder.mappingType(attribute);
                                // 如果类型不相等则备份老字段创建新字段
                                if (!type.equals(column.getType())) {
                                    String backupColumnSql = tableSqlBuilder.backupColumn(table, column);
                                    sqlList.add(backupColumnSql);
                                    String addColumnSql = tableSqlBuilder.addColumn(table, attribute);
                                    sqlList.add(addColumnSql);
                                }
                                break;
                            }
                            if (i == size - 1) {
                                // 没找到该字段就添加该字段
                                String addColumnSql = tableSqlBuilder.addColumn(table, attribute);
                                sqlList.add(addColumnSql);
                            }
                        }
                    }
                }
            } else {
                sqlList.add(sql);
            }
        }
        sqlList.forEach(sql -> log.info("execute sql: {}", sql));
        Db.use().executeBatch(sqlList);
    }
}
