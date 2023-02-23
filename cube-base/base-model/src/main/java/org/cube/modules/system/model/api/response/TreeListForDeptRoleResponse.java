package org.cube.modules.system.model.api.response;

import org.cube.modules.system.model.TreeModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TreeListForDeptRoleResponse {

    /**
     * 菜单列表
     */
    private List<TreeModel> treeList;

    /**
     * 所有菜单id
     */
    private List<String> ids;
}
