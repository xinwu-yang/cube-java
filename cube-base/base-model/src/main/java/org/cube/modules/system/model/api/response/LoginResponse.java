package org.cube.modules.system.model.api.response;

import org.cube.modules.system.entity.SysDepart;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.model.DictModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LoginResponse {

    /**
     * 登录凭证
     */
    private String token;

    /**
     * 凭证过期时间
     */
    private Long tokenTimeout;

    /**
     * 用户信息
     */
    private SysUser userInfo;

    /**
     * 所有的数据字典数据
     */
    private Map<String, List<DictModel>> sysAllDictItems;

    /**
     * 用户角色
     */
    private List<String> userRoles;

    /**
     * 用户所属部门
     */
    private List<SysDepart> departs;
}
