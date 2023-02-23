package org.cube.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.cube.modules.system.entity.SysAnnouncement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 系统通告
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysAnnouncementMapper extends BaseMapper<SysAnnouncement> {

    List<SysAnnouncement> querySysCementListByUserId(Page<SysAnnouncement> page, @Param("userId") String userId, @Param("msgCategory") int msgCategory);
}
