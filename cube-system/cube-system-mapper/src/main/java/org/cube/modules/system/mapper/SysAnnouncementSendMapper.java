package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.modules.system.model.AnnouncementSendModel;
import org.apache.ibatis.annotations.Param;
import org.cube.modules.system.entity.SysAnnouncementSend;

import java.util.List;

/**
 * 用户通告阅读标记
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysAnnouncementSendMapper extends BaseMapper<SysAnnouncementSend> {

    /**
     * 查询指定用户的所有系统通告id
     */
    List<String> queryByUserId(@Param("userId") String userId);

    /**
     * 获取我的消息
     */
    List<AnnouncementSendModel> getMyAnnouncementSendList(Page<AnnouncementSendModel> page, @Param("announcementSendModel") AnnouncementSendModel announcementSendModel);
}
