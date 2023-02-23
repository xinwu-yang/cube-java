package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 重复校验数据模型
 *
 * @author 张代浩
 * @version V1.0
 * @since 2019-03-25
 */
@Data
public class DuplicateCheckRequest implements Serializable {

    /**
     * 表名
     */
    @NotEmpty
    @Schema(title = "表名")
    private String tableName;

    /**
     * 字段名
     */
    @NotEmpty
    @Schema(title = "字段名")
    private String fieldName;

    /**
     * 字段值
     */
    @NotEmpty
    @Schema(title = "字段值")
    private String fieldVal;

    /**
     * 排除的数据ID
     */
    @Schema(title = "排除的数据ID")
    private String dataId;
}