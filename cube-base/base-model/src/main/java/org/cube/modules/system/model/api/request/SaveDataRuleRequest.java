package org.cube.modules.system.model.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SaveDataRuleRequest {

    /**
     * 菜单id
     */
    @NotBlank
    private String permissionId;

    /**
     * 部门id
     */
    @NotBlank
    private String departId;

    /**
     * 数据权限id数组，逗号分割
     */
    private String dataRuleIds;
}
