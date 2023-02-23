package org.cube.modules.system.enhancement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.entity.SysArea;

public interface ISysAreaService extends IService<SysArea> {
    /**
     * 新增地区
     *
     * @param sysArea 地区内容
     * @return 结果
     */
    Result<?> addSysArea(SysArea sysArea);

    /**
     * 删除地区
     *
     * @return 结果
     */
    Result<?> deleteSysArea(Long id, Long parentId);
}
