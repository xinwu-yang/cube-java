package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.base.Result;
import org.cube.modules.system.mapper.SysDepartRoleMapper;
import org.cube.modules.system.mapper.SysDepartRoleUserMapper;
import org.cube.modules.system.service.ISysDepartRoleService;
import org.cube.modules.system.entity.SysDepartRole;
import org.cube.modules.system.entity.SysDepartRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDepartRoleServiceImpl extends ServiceImpl<SysDepartRoleMapper, SysDepartRole> implements ISysDepartRoleService {

    @Autowired
    private SysDepartRoleUserMapper sysDepartRoleUserMapper;

    @Override
    public List<SysDepartRole> queryDeptRoleByDeptAndUser(String orgCode, String userId) {
        return baseMapper.queryDeptRoleByDeptAndUser(orgCode, userId);
    }

    @Override
    public Result<?> delete(List<String> ids) {
        for (String id : ids) {
            SysDepartRole sysDepartRole = baseMapper.selectById(id);
            if (sysDepartRole == null) {
                return Result.error("该部门角色[" + id + "]不存在！");
            }
            // 检查是否有用户还在使用这个职位
            QueryWrapper<SysDepartRoleUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("drole_id", sysDepartRole.getId());
            Long count = sysDepartRoleUserMapper.selectCount(queryWrapper);
            if (count != null && count > 0) {
                return Result.error("存在是该部门角色[" + sysDepartRole.getRoleName() + "]的用户！");
            }
        }
        baseMapper.deleteBatchIds(ids);
        return Result.ok();
    }
}
