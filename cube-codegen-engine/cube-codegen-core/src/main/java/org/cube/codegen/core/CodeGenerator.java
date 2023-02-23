package org.cube.codegen.core;

import org.cube.codegen.annotations.SubTables;
import org.cube.codegen.core.creator.TableCreator;
import org.cube.codegen.dbsync.DBSync;
import org.cube.codegen.core.models.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 代码生成器
 */
@Data
@AllArgsConstructor
public class CodeGenerator {
    // Java实体类路径
    private String className;

    /**
     * 生成代码
     *
     * @return Table
     * @throws ClassNotFoundException 找不到实体类
     */
    public Table generate() throws ClassNotFoundException, SQLException {
        Class<?> cls = Class.forName(className);
        List<Class<?>> classes = new ArrayList<>();
        classes.add(cls);
        // 同步数据库表
        SubTables subTables = cls.getDeclaredAnnotation(SubTables.class);
        if (subTables != null) {
            classes.addAll(Arrays.asList(subTables.value()));
        }
        DBSync.sync(classes);

        TableCreator tableCreator = new TableCreator(cls);
        return tableCreator.create();
    }
}
