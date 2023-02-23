package org.cube.codegen.dbsync.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Column {
    // 字段名
    private String field;
    // 字段类型
    private String type;
    // 是否允许null值
    private boolean allowNullValue;
    // 是否是主键
    private boolean isPK;
}
