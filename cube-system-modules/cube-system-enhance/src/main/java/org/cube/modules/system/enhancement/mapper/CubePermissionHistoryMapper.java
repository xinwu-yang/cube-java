package org.cube.modules.system.enhancement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.modules.system.enhancement.entity.SysPermissionHistory;
import org.cube.modules.system.enhancement.model.PermissionClickCount;
import org.cube.modules.system.enhancement.model.SysPermissionHistoryInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单历史记录Mapper
 */
@Repository
public interface CubePermissionHistoryMapper extends BaseMapper<SysPermissionHistory> {
    /**
     * 查询菜单点击次数
     *
     * @return PermissionClickCount
     */
    List<PermissionClickCount> queryPermissionClickCount(@Param("query") SysPermissionHistoryInfo query, Page<PermissionClickCount> page);
}
