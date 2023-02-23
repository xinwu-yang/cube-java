package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页请求数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Serializable {

    @Min(1)
    @Schema(title = "页码")
    private Integer pageNo = 1;

    @Min(1)
    @Schema(title = "每页数量")
    private Integer pageSize = 10;
}
