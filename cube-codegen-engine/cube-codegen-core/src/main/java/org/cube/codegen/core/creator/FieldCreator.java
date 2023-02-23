package org.cube.codegen.core.creator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.cube.codegen.annotations.ComponentParam;
import org.cube.codegen.annotations.FormField;
import org.cube.codegen.core.models.Component;
import org.cube.codegen.annotations.models.ComponentType;
import org.cube.codegen.core.models.Table;
import org.cube.codegen.core.models.TableField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 属性构建器
 */
public class FieldCreator extends AbstractCreator<List<TableField>> {
    private static final List<String> FORM_EXCLUDE_FIELD = ListUtil.of("id", "createBy", "createTime", "updateBy", "updateTime", "sysOrgCode", "delFlag");
    private static final List<String> LIST_EXCLUDE_FIELD = ListUtil.of("id", "sysOrgCode", "delFlag");

    public FieldCreator(Class<?> cls, Table table) {
        super(cls, table);
    }

    @Override
    public List<TableField> create() {
        List<TableField> fieldList = new ArrayList<>();
        // 分析字段
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            // 排除static字段
            if (!Modifier.isStatic(field.getModifiers())) {
                String fieldName = field.getName();
                Component component = new Component();
                String fieldTypeName = field.getType().getName().toLowerCase();
                ComponentType fieldType = analyzeFieldType(fieldTypeName);
                component.setName(fieldType);
                String basicType = analyzeFieldBasicType(fieldTypeName);
                component.setBasicType(basicType);

                TableField tableField = new TableField();
                tableField.setTitle(fieldName);
                tableField.setDataIndex(fieldName);
                tableField.setComponent(component);
                tableField.setShowInList(true);
                tableField.setShowInForm(true);
                tableField.setRequire(true);
                tableField.setGroupId(0);
                FormField formField = field.getDeclaredAnnotation(FormField.class);
                if (formField != null) {
                    if (StrUtil.isNotEmpty(formField.title())) {
                        tableField.setTitle(formField.title());
                    }
                    tableField.setSort(formField.sort());
                    tableField.setGroupId(formField.groupId());
                    tableField.setRequire(formField.require());
                    // 检查是否配置该GroupId
                    if (table.isGrouped() && formField.groupId() > 0) {
                        long count = table.getTableGroupList().stream().filter(tableGroup -> tableGroup.getId() == formField.groupId()).count();
                        if (count < 1) {
                            throw new IllegalArgumentException("字段：" + tableField.getDataIndex() + "，不存在分组Id：" + formField.groupId());
                        }
                    }
                    tableField.setShowInList(formField.showInList());
                    tableField.setShowInForm(formField.showInForm());
                    if (!StrUtil.hasEmpty(formField.dataIndex())) {
                        tableField.setDataIndex(formField.dataIndex());
                    }
                    analyzeComponent(formField, component);
                }
                // 这几个字段不出现在表单中
                if (excludeFieldInForm(fieldName)) {
                    tableField.setShowInForm(false);
                }
                // 这几个字段不出现在列表中
                if (excludeFieldInList(fieldName)) {
                    tableField.setShowInList(false);
                }
                fieldList.add(tableField);
            }
        }
        table.setFieldList(fieldList);
        return fieldList;
    }

    /**
     * 分析字段的数据类型
     *
     * @param fieldType java类型
     * @return 前端类型
     */
    private ComponentType analyzeFieldType(String fieldType) {
        if (fieldType.contains("int") || fieldType.contains("long")) {
            return ComponentType.NUMBER;
        } else if (fieldType.contains("date")) {
            return ComponentType.DATE;
        } else {
            return ComponentType.STRING;
        }
    }

    /**
     * 分析字段的基础数据类型
     *
     * @param fieldType java类型
     * @return 前端类型
     */
    private String analyzeFieldBasicType(String fieldType) {
        if (fieldType.contains("int")) {
            return "Integer";
        } else if (fieldType.contains("long")) {
            return "Long";
        } else if (fieldType.contains("date")) {
            return "Date";
        } else if (fieldType.contains("byte")) {
            return "byte[]";
        } else {
            return "String";
        }
    }

    /**
     * 获取字段带的组件
     *
     * @param field     字段
     * @param component 基础组件
     */
    private void analyzeComponent(FormField field, Component component) {
        // 如果没有设置数据类型这里还是使用基础类型
        if (ComponentType.NONE.equals(field.component())) {
            return;
        }
        component.setName(field.component());
        ComponentParam[] componentDataArray = field.componentParams();
        if (componentDataArray.length > 0) {
            Map<String, String> componentParams = MapUtil.newHashMap(componentDataArray.length);
            for (ComponentParam data : componentDataArray) {
                componentParams.put(data.key(), data.value());
            }
            component.setParams(componentParams);
        }
    }

    /**
     * 这几个字段不出现在表单中
     *
     * @param fieldName 字段名
     * @return 是否在表单中
     */
    private boolean excludeFieldInForm(String fieldName) {
        return FORM_EXCLUDE_FIELD.contains(fieldName);
    }

    /**
     * 这几个字段不出现在列表中
     *
     * @param fieldName 字段名
     * @return 是否在列表中
     */
    private boolean excludeFieldInList(String fieldName) {
        return LIST_EXCLUDE_FIELD.contains(fieldName);
    }
}
