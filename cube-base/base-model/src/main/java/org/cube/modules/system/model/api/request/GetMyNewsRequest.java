package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetMyNewsRequest extends PageRequest implements Serializable {

    @Schema(title = "标题")
    private String title;

    @Schema(title = "发布人")
    private String sender;

    @Schema(title = "阅读状态")
    private String readFlag;

    @Schema(title = "消息类型", description = "1:通知公告、2:系统消息")
    private String msgCategory;

    @Schema(title = "业务类型")
    private String busType;
}
