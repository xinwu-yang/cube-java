package org.cube.modules.system.model.api.response;

import org.cube.modules.system.entity.SysPermissionDataRule;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LoadDataRuleResponse {

    /**
     * 菜单下的数据权限
     */
    private List<SysPermissionDataRule> datarule;

    /**
     * 菜单的数据权限id
     */
    private String drChecked;
}
