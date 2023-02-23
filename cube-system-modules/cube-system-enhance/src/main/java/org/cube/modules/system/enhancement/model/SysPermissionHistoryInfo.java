package org.cube.modules.system.enhancement.model;

import org.cube.modules.system.model.api.request.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysPermissionHistoryInfo extends PageRequest {

    @Schema(title = "用户id")
    private String userId;

    @Schema(title = "部门id")
    private String departId;

    @Schema(title = "开始时间")
    private String begin;

    @Schema(title = "结束时间")
    private String end;
}
