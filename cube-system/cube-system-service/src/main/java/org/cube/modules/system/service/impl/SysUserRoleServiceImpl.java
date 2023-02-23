package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.entity.SysUserRole;
import org.cube.modules.system.mapper.SysUserRoleMapper;
import org.cube.modules.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public List<String> getRoleByUserName(String username) {
        return baseMapper.getRoleByUserName(username);
    }

    @Override
    public List<String> getRoleIdByUserName(String username) {
        return baseMapper.getRoleIdByUserName(username);
    }
}
