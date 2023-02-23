package org.cube.codegen.dbsync.models;

import lombok.Data;

import java.util.List;

@Data
public class Table {
    // 表名
    private String name;
    // 注释
    private String comment;
    // 主键
    private String primaryKey;
    // 强制同步
    private boolean forcedSync;
    // 表字段
    private List<Attribute> attributes;
}
