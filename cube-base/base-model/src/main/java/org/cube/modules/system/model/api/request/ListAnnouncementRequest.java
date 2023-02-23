package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListAnnouncementRequest extends PageRequest {

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
}
