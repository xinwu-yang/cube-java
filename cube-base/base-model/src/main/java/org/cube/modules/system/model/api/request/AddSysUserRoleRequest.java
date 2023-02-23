package org.cube.modules.system.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSysUserRoleRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    private String roleId;

    /**
     * 对应的用户id集合
     */
    private List<String> userIdList;
}
