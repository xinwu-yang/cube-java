package org.cube.modules.system.enhancement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IdParams implements Serializable {

    @Schema(title = "菜单id", description = "多个逗号分隔")
    private List<String> permissionIds;

    @Schema(title = "用户id", description = "多个逗号分隔")
    private List<String> userIds;

    @Schema(title = "角色id", description = "多个逗号分隔")
    private List<String> roleIds;

    @Schema(title = "字典id", description = "多个逗号分隔")
    private List<String> dictIds;
}
