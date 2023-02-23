package org.cube.modules.system.enhancement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.model.PermissionClickCount;
import org.cube.modules.system.enhancement.entity.SysPermissionHistory;
import org.cube.modules.system.enhancement.model.SysPermissionHistoryInfo;

public interface ISysPermissionHistoryService extends IService<SysPermissionHistory> {

    /**
     * 收集用户点击菜单的信息
     *
     * @param fullPath 点击的路径
     * @return ok
     */
    Result<?> collect(String fullPath);

    /**
     * 菜单点击次数统计
     *
     * @return ok
     */
    Result<?> permissionClickCount(SysPermissionHistoryInfo sysPermissionHistoryInfo, Page<PermissionClickCount> page);
}
