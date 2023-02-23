package org.cube.commons.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 带业务参数的消息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusMessageDTO extends MessageDTO implements Serializable {

    /**
     * 业务类型
     */
    private String busType;

    /**
     * 业务id
     */
    private String busId;

    /**
     * 构造 带业务参数的消息
     */
    public BusMessageDTO(String fromUser, String toUser, String title, String msgContent, Integer msgCategory, String busType, String busId) {
        super(fromUser, toUser, title, msgContent, msgCategory);
        this.busId = busId;
        this.busType = busType;
    }
}
