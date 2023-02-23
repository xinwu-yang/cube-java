package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class AddAnnouncementRequest implements Serializable {

    @Schema(title = "标题")
    private String title;

    @Schema(title = "发布人")
    private String sender;

    @Schema(title = "优先级")
    private String priority;

    @Schema(title = "消息类型", description = "1:通知公告、2:系统消息")
    private Integer msgCategory;

    @Schema(title = "通告对象类型", description = "USER:指定用户、ALL:全体用户")
    private String msgType;

    @Schema(title = "发布状态", description = "0:未发布、1:已发布、2:已撤销")
    private Integer sendStatus;

    @Schema(title = "排序字段")
    private String column;

    @Schema(title = "排序方式", description = "asc,desc", defaultValue = "desc")
    private String order;

    @Schema(title = "内容")
    private String msgContent;

    @Schema(title = "开始时间")
    private Date startTime;

    @Schema(title = "结束时间")
    private Date endTime;

    @Schema(title = "发布时间")
    private Date sendTime;

    @Schema(title = "撤销时间")
    private Date cancelTime;

    @Schema(title = "指定用户")
    private String userIds;

    @Schema(title = "业务类型", description = "email:邮件、bpm:流程")
    private String busType;

    @Schema(title = "业务id")
    private String busId;

    @Schema(title = "打开方式", description = "component:组件、url:路由")
    private String openType;

    @Schema(title = "组件/路由地址")
    private String openPage;

    @Schema(title = "摘要")
    private String msgAbstract;
}
