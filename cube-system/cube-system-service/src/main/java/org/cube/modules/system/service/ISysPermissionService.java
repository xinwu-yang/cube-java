package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.exception.CubeAppException;
import org.cube.modules.system.entity.SysPermission;
import org.cube.modules.system.model.TreeModel;

import java.util.List;

/**
 * 菜单权限
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<TreeModel> queryListByParentId(String parentId);

    /**
     * 真实删除
     */
    void deletePermission(String id) throws CubeAppException;

    /**
     * 逻辑删除
     */
    void deletePermissionLogical(String id) throws CubeAppException;

    void addPermission(SysPermission sysPermission) throws CubeAppException;

    void editPermission(SysPermission sysPermission) throws CubeAppException;

    List<SysPermission> queryByUser(String username);

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     */
    void deletePermRuleByPermId(String id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     */
    List<String> queryPermissionUrlWithStar();

    /**
     * 判断用户否拥有权限
     */
    boolean hasPermission(String username, SysPermission sysPermission);

    /**
     * 根据用户和请求地址判断是否有此权限
     */
    boolean hasPermission(String username, String url);

    /**
     * 根据用户账号查询菜单权限
     */
    int queryCountByUsername(String username, SysPermission sysPermission);
}
