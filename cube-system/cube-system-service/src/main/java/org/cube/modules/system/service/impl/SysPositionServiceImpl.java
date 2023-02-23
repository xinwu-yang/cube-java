package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysPosition;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.mapper.SysPositionMapper;
import org.cube.modules.system.mapper.SysUserMapper;
import org.cube.modules.system.service.ISysPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional
    public Result<?> delete(List<String> positionIds) {
        for (String id : positionIds) {
            SysPosition sysPosition = baseMapper.selectById(id);
            if (sysPosition == null) {
                return Result.error("该职位[" + id + "]不存在！");
            }
            // 检查是否有用户还在使用这个职位
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("post", sysPosition.getCode());
            Long count = sysUserMapper.selectCount(queryWrapper);
            if (count != null && count > 0) {
                return Result.error("存在是该职位[" + sysPosition.getName() + "]的用户！");
            }
        }
        baseMapper.deleteBatchIds(positionIds);
        return Result.ok();
    }
}
