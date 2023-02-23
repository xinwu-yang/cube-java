package org.cube.modules.system.model;

import org.cube.commons.annotations.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户通告阅读标记表
 *
 * @author xinwuy
 * @version V1.0
 * @since 2019-02-21
 */
@Data
@NoArgsConstructor
public class AnnouncementSendModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Schema(title = "主键")
    private String id;

    /**
     * 通告id
     */
    @Schema(title = "通告id")
    private String anntId;

    /**
     * 用户id
     */
    @Schema(title = "用户id")
    private String userId;

    /**
     * 标题
     */
    @Schema(title = "标题")
    private String title;

    /**
     * 内容
     */
    @Schema(title = "内容")
    private String msgContent;

    /**
     * 发布人
     */
    @Schema(title = "发布人")
    private String sender;

    /**
     * 优先级（L低，M中，H高）
     */
    @Dict("priority")
    @Schema(title = "优先级", description = "L:低、M:中、H:高")
    private String priority;

    /**
     * 阅读状态
     */
    @Schema(title = "阅读状态")
    private String readFlag;

    /**
     * 发布时间
     */
    @Schema(title = "发布时间")
    private Date sendTime;

    /**
     * 页数
     */
    @Schema(title = "页数")
    private Integer pageNo;

    /**
     * 大小
     */
    @Schema(title = "大小")
    private Integer pageSize;

    /**
     * 消息类型（1:通知公告、2:系统消息）
     */
    @Dict("msg_category")
    @Schema(title = "消息类型", description = "1:通知公告、2:系统消息")
    private String msgCategory;

    /**
     * 业务id
     */
    @Schema(title = "业务id")
    private String busId;

    /**
     * 业务类型
     */
    @Schema(title = "业务类型")
    private String busType;

    /**
     * 打开方式 组件：component 路由：url
     */
    @Schema(title = "打开方式", description = "component:组件、url:路由")
    private String openType;

    /**
     * 组件、路由、地址
     */
    @Schema(title = "组件、路由、地址")
    private String openPage;

    /**
     * 业务类型查询（0:非bpm业务）
     */
    @Schema(title = "业务类型查询", description = "0:非bpm业务")
    private String bizSource;

    /**
     * 摘要
     */
    @Schema(title = "摘要")
    private String msgAbstract;
}
