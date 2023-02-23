package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.modules.system.model.TreeModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.cube.modules.system.entity.SysPermission;

import java.util.List;

/**
 * 菜单权限
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 通过父菜单ID查询子菜单
     */
    List<TreeModel> queryListByParentId(@Param("parentId") String parentId);

    /**
     * 根据用户查询用户权限
     */
    List<SysPermission> queryByUser(@Param("username") String username);

    /**
     * 修改菜单状态字段： 是否子节点
     */
    @Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
    int setMenuLeaf(@Param("id") String id, @Param("leaf") int leaf);

    /**
     * 获取模糊匹配规则的数据权限URL
     */
    @Select("SELECT url FROM sys_permission WHERE del_flag = 0 and menu_type = 2 and url like '%*%'")
    List<String> queryPermissionUrlWithStar();


    /**
     * 根据用户账号查询菜单权限
     */
    int queryCountByUsername(@Param("username") String username, @Param("permission") SysPermission sysPermission);
}
