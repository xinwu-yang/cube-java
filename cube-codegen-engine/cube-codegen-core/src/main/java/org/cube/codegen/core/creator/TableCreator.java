package org.cube.codegen.core.creator;

import java.util.ArrayList;
import java.util.List;

import org.cube.codegen.core.models.Table;

/**
 * 表单构建器
 */
public class TableCreator extends AbstractCreator<Table> {

    public TableCreator(Class<?> cls) {
        super(cls, null);
    }

    @Override
    public Table create() {
        Table table = new Table();
        List<AbstractCreator<?>> creators = new ArrayList<>();
        creators.add(new JavaCodeParamsCreator(cls, table));
        creators.add(new GroupCreator(cls, table));
        creators.add(new QueryCreator(cls, table));
        creators.add(new FieldCreator(cls, table));
        creators.add(new SubTableCreator(cls, table));
        creators.forEach(AbstractCreator::create);
        return table;
    }
}
