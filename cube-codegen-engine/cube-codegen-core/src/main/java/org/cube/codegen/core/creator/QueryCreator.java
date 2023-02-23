package org.cube.codegen.core.creator;

import cn.hutool.core.map.MapUtil;
import org.cube.codegen.annotations.ComponentParam;
import org.cube.codegen.annotations.QueryField;
import org.cube.codegen.annotations.QueryFields;
import org.cube.codegen.core.models.Component;
import org.cube.codegen.core.models.Table;
import org.cube.codegen.core.models.TableQueryField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询条件构建器
 */
public class QueryCreator extends AbstractCreator<List<TableQueryField>> {

    public QueryCreator(Class<?> cls, Table table) {
        super(cls, table);
    }

    @Override
    public List<TableQueryField> create() {
        List<TableQueryField> tableQueryFields = new ArrayList<>();
        // 分析查询条件
        QueryFields queryFields = cls.getDeclaredAnnotation(QueryFields.class);
        if (queryFields != null) {
            table.setTableQueryFieldList(getQueryFields(queryFields));
        }
        return tableQueryFields;
    }

    /**
     * 获取查询区域信息
     *
     * @param queryFields QueryFields注解配置
     * @return TableQueryField
     */
    private List<TableQueryField> getQueryFields(QueryFields queryFields) {
        List<TableQueryField> tableQueryFields = new ArrayList<>();
        QueryField[] tableQueryFieldArray = queryFields.value();
        for (QueryField queryField : tableQueryFieldArray) {
            Component component = new Component();
            component.setName(queryField.component());
            ComponentParam[] componentDataArray = queryField.componentData();
            if (componentDataArray.length > 0) {
                Map<String, String> componentData = MapUtil.newHashMap(componentDataArray.length);
                for (ComponentParam data : componentDataArray) {
                    componentData.put(data.key(), data.value());
                }
                component.setParams(componentData);
            }
            TableQueryField tableQueryField = new TableQueryField(queryField.value(), queryField.label(), component, queryField.useFieldComponent());
            tableQueryFields.add(tableQueryField);
        }
        return tableQueryFields;
    }
}
