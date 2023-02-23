package org.cube.codegen.core.creator;

import org.cube.codegen.annotations.Group;
import org.cube.codegen.annotations.Groups;
import org.cube.codegen.core.models.Table;
import org.cube.codegen.core.models.TableGroup;

import java.util.List;

/**
 * 分组构建器
 */
public class GroupCreator extends AbstractCreator<List<TableGroup>> {

    public GroupCreator(Class<?> cls, Table table) {
        super(cls, table);
    }

    @Override
    public List<TableGroup> create() {
        List<TableGroup> tableGroups = this.table.getTableGroupList();
        Groups groups = cls.getDeclaredAnnotation(Groups.class);
        if (groups != null) {
            Group[] groupArray = groups.value();
            for (Group group : groupArray) {
                TableGroup tableGroup = new TableGroup(group.id(), group.name());
                tableGroups.add(tableGroup);
            }
            table.setTableGroupList(tableGroups);
        }
        return tableGroups;
    }
}
