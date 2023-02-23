package org.cube.codegen.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableGroup {

    /**
     * 序号
     */
    private int id;

    /**
     * 名称
     */
    private String name;
}
