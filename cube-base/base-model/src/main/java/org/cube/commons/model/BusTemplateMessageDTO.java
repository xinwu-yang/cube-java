package org.cube.commons.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 带业务参数的模板消息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusTemplateMessageDTO extends TemplateMessageDTO implements Serializable {

    /**
     * 业务类型
     */
    private String busType;

    /**
     * 业务id
     */
    private String busId;

    /**
     * 构造 带业务参数的模板消息
     *
     * @param fromUser
     * @param toUser
     * @param title
     * @param busType
     * @param busId
     */
    public BusTemplateMessageDTO(String fromUser, String toUser, String title, String content, String busType, String busId) {
        super(fromUser, toUser, title, content);
        this.busId = busId;
        this.busType = busType;
    }
}
