package org.cube.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.base.Result;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.enums.OperateLogType;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.crypto.PasswordUtil;
import org.cube.modules.system.entity.*;
import org.cube.modules.system.mapper.*;
import org.cube.modules.system.model.SysUserDepVo;
import org.cube.modules.system.model.SysUserSysDepartModel;
import org.cube.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysDepartRoleUserMapper departRoleUserMapper;
    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;

    @Override
    public void add(SysUser user, String selectedRoles, String selectedDeparts) {
        String salt = RandomUtil.randomString(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
        user.setPassword(passwordEncode);
        user.setStatus(1);
        user.setDelFlag(0);
        user.setFirstLogin(true);
        this.addUserWithRole(user, selectedRoles);
        this.addUserWithDepart(user, selectedDeparts);
    }

    @Override
    public Result<?> changePassword(String oldPassword, String newPassword, String confirmPassword) {
        String username = StpUtil.getLoginIdAsString();
        SysUser user = baseMapper.getUserByName(username);
        String passwordEncode = PasswordUtil.encrypt(username, oldPassword, user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            return Result.error("旧密码输入错误！");
        }
        if (StrUtil.isEmpty(newPassword)) {
            return Result.error("新密码不允许为空！");
        }
        if (!newPassword.equals(confirmPassword)) {
            return Result.error("两次输入密码不一致！");
        }
        if (oldPassword.equals(confirmPassword)) {
            return Result.error("新密码不能和老密码一样！");
        }
        String password = PasswordUtil.encrypt(username, newPassword, user.getSalt());
        baseMapper.update(new SysUser().setPassword(password).setFirstLogin(false), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        return Result.ok();
    }

    @Override
    public Result<?> changePassword(SysUser sysUser) {
        String salt = RandomUtil.randomString(8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setPassword(passwordEncode);
        baseMapper.updateById(sysUser);
        return Result.ok();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheConst.SYS_USERS_CACHE, allEntries = true)
    public void deleteUser(String userId) {
        this.removeById(userId);
        SystemContextUtil.log("删除用户，userId： " + userId, OperateLogType.DELETE);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheConst.SYS_USERS_CACHE, allEntries = true)
    public void deleteBatchUsers(String userIds) {
        this.removeBatchByIds(ListUtil.of(userIds.split(",")));
        SystemContextUtil.log("批量删除用户，userIds： " + userIds, OperateLogType.DELETE);
    }

    @Override
    public SysUser getUserByName(String username) {
        return baseMapper.getUserByName(username);
    }

    @Override
    @Transactional
    public void addUserWithRole(SysUser user, String roles) {
        this.save(user);
        if (StrUtil.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @CacheEvict(cacheNames = CacheConst.SYS_USERS_CACHE, allEntries = true)
    @Transactional
    public void editUserWithRole(SysUser user, String roles) {
        this.updateById(user);
        //先删后加
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        if (StrUtil.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    public List<String> getRole(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    @Override
    public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username) {
        return baseMapper.getUserByDepId(page, departId, username);
    }

    @Override
    public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username) {
        if (StrUtil.isNotBlank(username)) {
            username = "%" + username + "%";
        }
        return baseMapper.getUserByDepIds(page, departIds, username);
    }

    @Override
    public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
        List<SysUserDepVo> list = this.baseMapper.getDepNamesByUserIds(userIds);
        Map<String, String> res = new HashMap<>();
        list.forEach(item -> res.merge(item.getUserId(), item.getDepartName(), (a, b) -> a + "," + b));
        return res;
    }

    @Override
    public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(SysUser::getDelFlag, CommonConst.NOT_DELETED);
        lambdaQueryWrapper.inSql(SysUser::getId, "SELECT user_id FROM sys_user_depart WHERE dep_id = '" + departId + "'");
        return baseMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public List<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage<?> page) {
        return baseMapper.getUserByOrgCode(page, orgCode, userParams);
    }

    @Override
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
        return baseMapper.getUserByRoleId(page, roleId, username);
    }

    @Override
    @CacheEvict(value = {CacheConst.SYS_USERS_CACHE}, key = "#username")
    public void updateUserDepart(String username, String orgCode) {
        baseMapper.updateUserDepart(username, orgCode);
    }

    @Override
    public SysUser getUserByPhone(String phone) {
        return baseMapper.getUserByPhone(phone);
    }


    @Override
    public SysUser getUserByEmail(String email) {
        return baseMapper.getUserByEmail(email);
    }

    @Override
    @Transactional
    public void addUserWithDepart(SysUser user, String selectedParts) {
        if (StrUtil.isNotEmpty(selectedParts)) {
            String[] arr = selectedParts.split(",");
            for (String departId : arr) {
                SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
                sysUserDepartMapper.insert(userDepart);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheConst.SYS_USERS_CACHE, allEntries = true)
    public void editUserWithDepart(SysUser user, String departs) {
        this.updateById(user);  //更新角色的时候已经更新了一次了，可以再跟新一次
        String[] arr = {};
        if (StrUtil.isNotEmpty(departs)) {
            arr = departs.split(",");
        }
        //查询已关联部门
        List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        if (userDepartList != null && userDepartList.size() > 0) {
            for (SysUserDepart depart : userDepartList) {
                //修改已关联部门删除部门用户角色关系
                if (!Arrays.asList(arr).contains(depart.getDepId())) {
                    List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId, depart.getDepId()));
                    List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
                    if (roleIds.size() > 0) {
                        departRoleUserMapper.delete(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, user.getId()).in(SysDepartRoleUser::getDroleId, roleIds));
                    }
                }
            }
        }
        //先删后加
        sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        if (StrUtil.isNotEmpty(departs)) {
            for (String departId : arr) {
                SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
                sysUserDepartMapper.insert(userDepart);
            }
        }
    }

    @Override
    public List<SysUser> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(SysUser::getDelFlag, CommonConst.DELETED);
        return baseMapper.selectLogicDeleted(wrapper);
    }

    @Override
    public boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity) {
        String ids = String.format("'%s'", String.join("','", userIds));
        return baseMapper.revertLogicDeleted(ids, updateEntity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> userIds) {
        String ids = String.format("'%s'", String.join("','", userIds));
        // 1. 删除用户
        int line = baseMapper.deleteLogicDeleted(ids);
        // 2. 删除用户部门关系
        line += sysUserDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getUserId, userIds));
        //3. 删除用户角色关系
        line += sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
        return line != 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNullPhoneEmail() {
        baseMapper.updateNullByEmptyString("email");
        baseMapper.updateNullByEmptyString("phone");
        return true;
    }

    @Override
    public void saveThirdUser(SysUser sysUser) {
        //保存用户
        String userId = IdUtil.simpleUUID();
        sysUser.setId(userId);
        baseMapper.insert(sysUser);
        //获取第三方角色
        SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, "third_role"));
        //保存用户角色
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(sysRole.getId());
        userRole.setUserId(userId);
        sysUserRoleMapper.insert(userRole);
    }

    @Override
    public List<SysUser> queryByDepIds(List<String> departIds, String username) {
        return baseMapper.queryByDepIds(departIds, username);
    }
}
