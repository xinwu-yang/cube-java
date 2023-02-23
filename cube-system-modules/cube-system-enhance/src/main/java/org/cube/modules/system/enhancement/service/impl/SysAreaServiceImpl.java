package org.cube.modules.system.enhancement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.entity.SysArea;
import org.cube.modules.system.enhancement.mapper.CubeAreaMapper;
import org.cube.modules.system.enhancement.service.ISysAreaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysAreaServiceImpl extends ServiceImpl<CubeAreaMapper, SysArea> implements ISysAreaService {

    @Override
    @Transactional
    public Result<?> addSysArea(final SysArea sysArea) {
        // 添加第一级菜单
        if (sysArea.getParentId() == null || sysArea.getParentId() == 0L) {
            sysArea.setParentId(0L);
            sysArea.setLevel(0);
        } else {// 添加下一级菜单
            final SysArea parentArea = baseMapper.selectById(sysArea.getParentId());
            if (parentArea == null) {
                return Result.error("父级菜单不存在！");
            }
            if (!parentArea.getHasChildren()) {
                parentArea.setHasChildren(true);
                baseMapper.updateById(parentArea);
            }
            sysArea.setLevel(parentArea.getLevel() + 1);
            if (parentArea.getLevel() == 0) {
                sysArea.setPath(String.valueOf(sysArea.getParentId()));
            } else {
                sysArea.setPath(parentArea.getPath() + "/" + sysArea.getParentId());
            }
        }
        sysArea.setHasChildren(false);
        sysArea.setDelFlag(0);
        baseMapper.insert(sysArea);
        return Result.ok("添加成功！");
    }

    @Override
    @Transactional
    public Result<?> deleteSysArea(Long id, Long parentId) {
        int result = baseMapper.deleteById(id);
        if (result > 0 && parentId > 0) {
            QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", parentId);
            queryWrapper.eq("del_flag", "0");
            long count = baseMapper.selectCount(queryWrapper);
            if (count < 1) {
                SysArea parentArea = baseMapper.selectById(parentId);
                parentArea.setHasChildren(false);
                baseMapper.updateById(parentArea);
            }
        }
        return Result.ok("删除成功！");
    }
}
