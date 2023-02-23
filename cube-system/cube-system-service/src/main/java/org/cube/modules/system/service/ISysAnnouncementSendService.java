package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.model.AnnouncementSendModel;
import org.cube.modules.system.entity.SysAnnouncementSend;

/**
 * 用户通告阅读标记表
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysAnnouncementSendService extends IService<SysAnnouncementSend> {

    /**
     * 获取我的消息
     */
    Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page, AnnouncementSendModel announcementSendModel);

    /**
     * 消息已读
     *
     * @param announcementId 消息id
     */
    void haveRead(Long announcementId);

    /**
     * 一键已读
     */
    void readAll();
}
