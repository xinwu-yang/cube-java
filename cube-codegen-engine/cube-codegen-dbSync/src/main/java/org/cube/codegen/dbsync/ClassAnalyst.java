package org.cube.codegen.dbsync;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.codegen.annotations.Form;
import org.cube.codegen.annotations.FormField;
import org.cube.codegen.annotations.db.DBField;
import org.cube.codegen.annotations.db.DefaultValue;
import org.cube.codegen.annotations.db.ForcedSync;
import org.cube.codegen.dbsync.models.Attribute;
import org.cube.codegen.dbsync.models.Table;
import org.cube.codegen.annotations.models.DefaultValueType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassAnalyst {

    /**
     * 实体类转化为通用的Table
     *
     * @param cls 字节码
     * @return Table
     */
    public static Table analyze(Class<?> cls) {
        Table table = new Table();
        // 类名
        String className = cls.getSimpleName();
        String tableName = NamingCase.toUnderlineCase(className).toLowerCase();
        TableName tableNameAnnotation = cls.getDeclaredAnnotation(TableName.class);
        if (tableNameAnnotation != null && !StrUtil.isEmpty(tableNameAnnotation.value())) {
            tableName = tableNameAnnotation.value();
        }
        table.setName(tableName);
        ForcedSync forcedSync = cls.getDeclaredAnnotation(ForcedSync.class);
        if (forcedSync != null) {
            table.setForcedSync(true);
        }
        Form form = cls.getDeclaredAnnotation(Form.class);
        if (form != null) {
            table.setComment(form.description());
        }
        List<Attribute> attributes = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                Attribute attribute = new Attribute();
                attributes.add(attribute);
                String fieldType;
                String fieldName = NamingCase.toUnderlineCase(field.getName()).toLowerCase();
                // 字段名称
                TableField tableField = field.getDeclaredAnnotation(TableField.class);
                if (tableField != null && !StrUtil.isEmpty(tableField.value())) {
                    fieldName = tableField.value();
                }
                attribute.setName(fieldName);
                // 字段注释
                FormField formField = field.getDeclaredAnnotation(FormField.class);
                if (formField != null) {
                    attribute.setComment(formField.title());
                }
                // 如果是主键
                TableId tableId = field.getDeclaredAnnotation(TableId.class);
                // 主键
                if (tableId != null) {
                    fieldType = tableId.type().name();
                    attribute.setType(fieldType);
                    table.setPrimaryKey(fieldName);
                    continue;
                }
                // 逻辑删除长度默认1
                TableLogic tableLogic = field.getDeclaredAnnotation(TableLogic.class);
                if (tableLogic != null) {
                    attribute.setLength(1);
                }
                // 重写数据类型
                DBField dbField = field.getDeclaredAnnotation(DBField.class);
                if (dbField != null) {
                    attribute.setAllowNullValue(dbField.allowNullValue());
                    if (StrUtil.isNotBlank(dbField.type())) {
                        attribute.setOverrideType(dbField.type());
                    }
                    if (dbField.length() > 0) {
                        attribute.setLength(dbField.length());
                    }
                }
                // 重写默认值
                DefaultValue defaultValue = field.getDeclaredAnnotation(DefaultValue.class);
                if (defaultValue != null) {
                    if (!defaultValue.type().equals(DefaultValueType.NONE)) {
                        attribute.setDefaultValue(defaultValue.value());
                        attribute.setDefaultValueType(defaultValue.type());
                    }
                }
                // 分析字段
                String fieldTypeSimpleName = field.getType().getSimpleName().toLowerCase();
                attribute.setType(fieldTypeSimpleName);
            }
        }
        table.setAttributes(attributes);
        return table;
    }
}
