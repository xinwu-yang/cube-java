package org.cube.modules.system.sms.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送消息实体
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-09-09
 */
@Data
public class MsgParams implements Serializable {
    private static final long serialVersionUID = 1L;

    /*消息类型*/
    private String msgType;
    /*消息接收方*/
    private String receiver;
    /*消息模板码*/
    private String templateCode;
    /*测试数据*/
    private String testData;
}
