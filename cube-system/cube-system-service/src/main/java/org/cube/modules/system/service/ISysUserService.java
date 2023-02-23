package org.cube.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.model.SysUserSysDepartModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 新增用户
     *
     * @param user            用户基础信息
     * @param selectedRoles   用户绑定的角色
     * @param selectedDeparts 用户绑定的部门
     */
    void add(SysUser user, String selectedRoles, String selectedDeparts);

    /**
     * 用户自己修改密码
     */
    Result<?> changePassword(String oldPassword, String newPassword, String confirmPassword);

    /**
     * 管理员修改密码
     */
    Result<?> changePassword(SysUser sysUser);

    /**
     * 删除用户
     */
    void deleteUser(String userId);

    /**
     * 批量删除用户
     */
    void deleteBatchUsers(String userIds);

    SysUser getUserByName(String username);

    /**
     * 添加用户和用户角色关系
     */
    void addUserWithRole(SysUser user, String roles);

    /**
     * 修改用户和用户角色关系
     */
    void editUserWithRole(SysUser user, String roles);

    /**
     * 获取用户的授权角色
     */
    List<String> getRole(String username);

    /**
     * 根据部门Id查询
     */
    IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

    /**
     * 根据部门Ids查询
     */
    IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username);

    /**
     * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）
     */
    Map<String, String> getDepNamesByUserIds(List<String> userIds);

    /**
     * 根据部门 Id 和 QueryWrapper 查询
     */
    IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper);

    /**
     * 根据 orgCode 查询用户，包括子部门下的用户
     *
     * @param orgCode    部门编码
     * @param userParams 用户查询条件，可为空
     * @param page       分页参数
     */
    List<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage<?> page);

    /**
     * 根据角色Id查询
     */
    IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

    /**
     * 根据用户名设置部门ID
     */
    void updateUserDepart(String username, String orgCode);

    /**
     * 根据手机号获取用户名和密码
     */
    SysUser getUserByPhone(String phone);

    /**
     * 根据邮箱获取用户
     */
    SysUser getUserByEmail(String email);

    /**
     * 添加用户和用户部门关系
     */
    void addUserWithDepart(SysUser user, String selectedParts);

    /**
     * 编辑用户和用户部门关系
     */
    void editUserWithDepart(SysUser user, String departs);

    /**
     * 查询被逻辑删除的用户
     */
    List<SysUser> queryLogicDeleted();

    /**
     * 查询被逻辑删除的用户（可拼装查询条件）
     */
    List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper);

    /**
     * 还原被逻辑删除的用户
     */
    boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity);

    /**
     * 彻底删除被逻辑删除的用户
     */
    boolean removeLogicDeleted(List<String> userIds);

    /**
     * 更新手机号、邮箱空字符串为 null
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateNullPhoneEmail();

    /**
     * 保存第三方用户信息
     */
    void saveThirdUser(SysUser sysUser);

    /**
     * 根据部门Ids查询
     */
    List<SysUser> queryByDepIds(List<String> departIds, String username);
}
