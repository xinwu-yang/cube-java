package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.entity.SysAnnouncement;
import org.cube.modules.system.model.api.response.AnnouncementListByUserResponse;

/**
 * 系统通告
 *
 * @author 杨欣武
 * @version 2.5.3
 * @since 2022-08-12
 */
public interface ISysAnnouncementService extends IService<SysAnnouncement> {

    /**
     * 保存系统通告
     *
     * @param announcement 通过内容
     */
    void saveAnnouncement(SysAnnouncement announcement);

    /**
     * 更新
     *
     * @param announcement 通过内容
     */
    void updateAnnouncement(SysAnnouncement announcement);

    /**
     * 通过用户id和消息类型查询通告列表
     *
     * @param page        分页信息
     * @param userId      用户id
     * @param msgCategory 通告分类
     */
    Page<SysAnnouncement> queryAnnouncementByUserId(Page<SysAnnouncement> page, String userId, int msgCategory);

    /**
     * 发布通告
     *
     * @param announcementId 通告id
     */
    void release(Long announcementId);

    /**
     * 撤回通告
     *
     * @param announcementId 通告id
     */
    void revoke(Long announcementId);

    /**
     * 查询用户数据并返回系统消息
     */
    AnnouncementListByUserResponse listByUser();

    /**
     * 同步消息
     *
     * @param announcementId 通告id
     */
    void syncNotice(Long announcementId);
}
