package org.cube.plugin.sqlexport.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.plugin.sqlexport.SQLExporter;
import org.cube.plugin.sqlexport.utils.NameStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLExporter implements SQLExporter {

    @Override
    public String insert(Object obj) {
        // 属性名拼接
        StringBuilder fieldNames = new StringBuilder();
        // 字段值拼接
        StringBuilder values = new StringBuilder();
        Class<?> cls = obj.getClass();
        TableName tableName = cls.getAnnotation(TableName.class);
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名
            String fieldName = field.getName();
            TableField tableField = field.getAnnotation(TableField.class);
            TableId tableId = field.getAnnotation(TableId.class);
            // 判断是否是忽略字段
            if (tableField != null && !tableField.exist()) {
                continue;
            }
            // 获取方法
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                // 获取方法名
                String methodName = method.getName();
                // 过滤筛选get方法
                if (methodName.startsWith("get") && !methodName.startsWith("getClass")) {
                    // 通过方法名截取出字段名
                    String getterName = methodName.substring(3);
                    // 判断前面的属性名 是否 与截取出来的字段名相同 ，则说明顺序相同 ，则拼接
                    if (fieldName.equalsIgnoreCase(getterName)) {
                        if (tableId != null && tableId.type().equals(IdType.AUTO)) {
                            continue;
                        }
                        if (tableId != null && !"".equals(tableId.value())) {
                            fieldName = tableId.value();
                        } else if (tableField != null) {
                            fieldName = tableField.value();
                        } else {
                            fieldName = NameStrategy.toUnderline(fieldName);
                        }
                        fieldNames.append(fieldName).append(",");
                        try {
                            // 方法类型
                            String methodReturnType = method.getReturnType().getSimpleName();
                            // 获取方法值
                            Object methodReturnValue = method.invoke(obj);
                            if (methodReturnValue == null) {
                                values.append("null");
                            } else {
                                switch (methodReturnType) {
                                    case "String":
                                        values.append("'").append(methodReturnValue.toString()).append("'");
                                        break;
                                    case "Date":
                                        Date date = (Date) methodReturnValue;
                                        LocalDateTime time = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                                        String formatTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                        values.append("'").append(formatTime).append("'");
                                        break;
                                    default:
                                        values.append(methodReturnValue.toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        values.append(",");
                    }
                }
            }
        }
        String fieldNamesStr = fieldNames.substring(0, fieldNames.length() - 1); //去除末尾的逗号
        String valuesStr = values.substring(0, values.length() - 1);
        String tableNameStr = NameStrategy.toUnderline(cls.getSimpleName());
        if (tableName != null) {
            tableNameStr = tableName.value();
        }
        return "insert into " + tableNameStr + "(" + fieldNamesStr + ")" + " values" + "(" + valuesStr + ");";
    }

    @Override
    public <T> List<String> inserts(List<T> objs) {
        List<String> sqlData = new ArrayList<>();
        for (Object obj : objs) {
            sqlData.add(insert(obj));
        }
        return sqlData;
    }

    @Override
    public String delete(Object obj) {
        Class<?> cls = obj.getClass();
        TableName tableName = cls.getAnnotation(TableName.class);
        Field[] fields = obj.getClass().getDeclaredFields();
        String IdFieldName = null;
        String IdFieldValue = null;
        for (Field field : fields) {
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId == null) {
                continue;
            }
            IdFieldName = tableId.value();
            if ("".equals(tableId.value())) {
                IdFieldName = NameStrategy.toUnderline(field.getName());
            }
            try {
                Method method = cls.getMethod(NameStrategy.toGetterName(field.getName()));
                // 方法类型
                String methodReturnType = method.getReturnType().getSimpleName();
                // 获取方法值
                String methodReturnValue = method.invoke(obj).toString();
                if ("String".equals(methodReturnType)) {
                    IdFieldValue = "'" + methodReturnValue + "'";
                } else {
                    IdFieldValue = methodReturnValue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        String tableNameStr = NameStrategy.toUnderline(cls.getSimpleName());
        if (tableName != null) {
            tableNameStr = tableName.value();
        }
        return "delete from " + tableNameStr + " where " + IdFieldName + "=" + IdFieldValue + ";";
    }

    @Override
    public <T> List<String> deletes(List<T> objs) {
        List<String> sqlData = new ArrayList<>();
        for (Object obj : objs) {
            sqlData.add(delete(obj));
        }
        return sqlData;
    }

    @Override
    public String foreignKeyChecks(boolean value) {
        return "SET FOREIGN_KEY_CHECKS=" + (value ? 0 : 1) + ";";
    }

    @Override
    public String comment(String content) {
        return "# " + content;
    }
}
