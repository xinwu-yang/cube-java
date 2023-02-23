package org.cube.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据日志传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataLogDTO implements Serializable {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 数据id
     */
    private String dataId;

    /**
     * 数据内容
     */
    private String dataContent;
}
