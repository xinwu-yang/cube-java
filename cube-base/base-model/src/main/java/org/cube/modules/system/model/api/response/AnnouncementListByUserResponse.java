package org.cube.modules.system.model.api.response;

import org.cube.modules.system.entity.SysAnnouncement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
     * 查询用户数据并返回系统消息
 */
@Data
@NoArgsConstructor
public class AnnouncementListByUserResponse {

    /**
     * 系统消息总数
     */
    private long sysMsgTotal;

    /**
     * 通告消息总数
     */
    private long anntMsgTotal;

    /**
     * 系统消息前五条
     */
    private List<SysAnnouncement> sysMsgList;

    /**
     * 通告消息前五条
     */
    private List<SysAnnouncement> anntMsgList;
}
