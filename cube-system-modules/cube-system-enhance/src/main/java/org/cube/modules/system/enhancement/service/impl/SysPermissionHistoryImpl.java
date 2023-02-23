package org.cube.modules.system.enhancement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.entity.SysPermissionHistory;
import org.cube.modules.system.enhancement.mapper.CubePermissionHistoryMapper;
import org.cube.modules.system.enhancement.model.PermissionClickCount;
import org.cube.modules.system.enhancement.model.SysPermissionHistoryInfo;
import org.cube.modules.system.enhancement.service.ISysPermissionHistoryService;
import org.cube.modules.system.entity.SysPermission;
import org.cube.modules.system.mapper.SysPermissionMapper;
import org.cube.modules.system.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SysPermissionHistoryImpl extends ServiceImpl<CubePermissionHistoryMapper, SysPermissionHistory> implements ISysPermissionHistoryService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    @Transactional
    public Result<?> collect(String fullPath) {
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("url", fullPath);
        SysPermission permission = sysPermissionMapper.selectOne(wrapper);
        if (Objects.nonNull(permission)) {
            LoginUser user = SystemContextUtil.currentLoginUser();
            SysPermissionHistory history = new SysPermissionHistory();
            history.setUserId(user.getId());
            history.setPermissionId(permission.getId());
            history.setCreateTime(LocalDateTime.now());
            baseMapper.insert(history);
        }
        return Result.ok();
    }

    @Override
    public Result<?> permissionClickCount(SysPermissionHistoryInfo sysPermissionHistoryInfo, Page<PermissionClickCount> page) {
        List<PermissionClickCount> permissionClickCounts = baseMapper.queryPermissionClickCount(sysPermissionHistoryInfo, page);
        page.setRecords(permissionClickCounts);
        return Result.ok(page);
    }
}
