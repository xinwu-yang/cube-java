package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.modules.system.model.SysUserDepVo;
import org.cube.modules.system.model.SysUserSysDepartModel;
import org.apache.ibatis.annotations.Param;
import org.cube.modules.system.entity.SysUser;

import java.util.List;

/**
 * 用户管理
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 通过用户账号查询用户信息
     */
    SysUser getUserByName(@Param("username") String username);

    /**
     * 根据部门Id查询用户信息
     */
    IPage<SysUser> getUserByDepId(Page<?> page, @Param("departId") String departId, @Param("username") String username);

    /**
     * 根据用户Ids,查询用户所属部门名称信息
     */
    List<SysUserDepVo> getDepNamesByUserIds(@Param("userIds") List<String> userIds);

    /**
     * 根据部门Ids,查询部门下用户信息
     */
    IPage<SysUser> getUserByDepIds(Page<?> page, @Param("departIds") List<String> departIds, @Param("username") String username);

    /**
     * 根据角色Id查询用户信息
     */
    IPage<SysUser> getUserByRoleId(Page<?> page, @Param("roleId") String roleId, @Param("username") String username);

    /**
     * 根据用户名设置部门ID
     */
    void updateUserDepart(@Param("username") String username, @Param("orgCode") String orgCode);

    /**
     * 根据手机号查询用户信息
     */
    SysUser getUserByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询用户信息
     */
    SysUser getUserByEmail(@Param("email") String email);

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     *
     * @param page       分页对象, xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param orgCode    部门编码
     * @param userParams 用户查询条件，可为空
     */
    List<SysUserSysDepartModel> getUserByOrgCode(IPage<?> page, @Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);

    /**
     * 查询 getUserByOrgCode 的Total
     *
     * @param orgCode    部门编码
     * @param userParams 用户查询条件，可为空
     */
    Integer getUserByOrgCodeTotal(@Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);

    /**
     * 批量删除角色与用户关系
     */
    void deleteBathRoleUserRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * 批量删除角色与权限关系
     */
    void deleteBathRolePermissionRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * 查询被逻辑删除的用户
     */
    List<SysUser> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);

    /**
     * 还原被逻辑删除的用户
     */
    int revertLogicDeleted(@Param("userIds") String userIds, @Param("entity") SysUser entity);

    /**
     * 彻底删除被逻辑删除的用户
     */
    int deleteLogicDeleted(@Param("userIds") String userIds);

    /**
     * 更新空字符串为null【此写法有sql注入风险，禁止随便用】
     */
    int updateNullByEmptyString(@Param("fieldName") String fieldName);

    /**
     * 根据部门Ids,查询部门下用户信息
     */
    List<SysUser> queryByDepIds(@Param("departIds") List<String> departIds, @Param("username") String username);
}
