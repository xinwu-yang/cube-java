package org.cube.codegen.core.creator;

import org.cube.codegen.core.models.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractCreator<T> {
    protected Class<?> cls;
    protected Table table;

    /**
     * 构建
     *
     * @return T
     */
    public abstract T create();
}
