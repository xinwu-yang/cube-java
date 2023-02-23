package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.mapper.SysRoleMapper;
import org.cube.modules.system.mapper.SysUserMapper;
import org.cube.modules.system.service.ISysRoleService;
import org.cube.modules.system.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleId) {
        //1.删除角色和用户关系
        baseMapper.deleteRoleUserRelation(roleId);
        //2.删除角色和权限关系
        baseMapper.deleteRolePermissionRelation(roleId);
        //3.删除角色
        this.removeById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchRole(String[] roleIds) {
        //1.删除角色和用户关系
        sysUserMapper.deleteBathRoleUserRelation(roleIds);
        //2.删除角色和权限关系
        sysUserMapper.deleteBathRolePermissionRelation(roleIds);
        //3.删除角色
        this.removeByIds(Arrays.asList(roleIds));
    }
}
