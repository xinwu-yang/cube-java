package org.cube.codegen.core.creator;

import cn.hutool.core.util.StrUtil;
import org.cube.codegen.annotations.ForeignKey;
import org.cube.codegen.annotations.SubTables;
import org.cube.codegen.core.models.SubTable;
import org.cube.codegen.core.models.Table;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 子表构建器
 */
public class SubTableCreator extends AbstractCreator<List<SubTable>> {

    public SubTableCreator(Class<?> cls, Table table) {
        super(cls, table);
    }

    @Override
    public List<SubTable> create() {
        List<SubTable> subTableList = this.table.getSubTableList();
        SubTables subTables = cls.getDeclaredAnnotation(SubTables.class);
        if (subTables != null) {
            Class<?>[] subTableClasses = subTables.value();
            for (Class<?> subTableClass : subTableClasses) {
                SubTable subTable = new SubTable();
                TableCreator tableCreator = new TableCreator(subTableClass);
                Table subTab = tableCreator.create();
                String entityName = subTab.getJavaCodeParams().getEntityName();
                subTable.setEntityName(entityName);
                subTable.setEntityNameLower(StrUtil.subPre(entityName, 1).toLowerCase() + StrUtil.subSuf(entityName, 1));
                subTable.setEntityPackage(subTableClass.getPackage().getName());
                subTable.setDescription(subTab.getJavaCodeParams().getDescription());
                subTable.setFieldList(subTab.getFieldList());
                subTable.setTableQueryFieldList(subTab.getTableQueryFieldList());
                Field[] fields = subTableClass.getDeclaredFields();
                for (Field field : fields) {
                    ForeignKey foreignKey = field.getDeclaredAnnotation(ForeignKey.class);
                    if (foreignKey != null && foreignKey.value().equals(cls)) {
                        subTable.setForeignKey(field.getName());
                        subTable.setRelationType(foreignKey.relationType());
                        break;
                    }
                }
                if (StrUtil.isEmpty(subTable.getForeignKey())) {
                    throw new IllegalArgumentException("No foreign key found in " + cls.getSimpleName() + ".");
                }
                subTableList.add(subTable);
            }
        }
        return subTableList;
    }
}
