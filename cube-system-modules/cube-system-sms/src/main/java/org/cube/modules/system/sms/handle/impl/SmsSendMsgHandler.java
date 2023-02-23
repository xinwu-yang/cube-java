package org.cube.modules.system.sms.handle.impl;

import org.cube.modules.system.sms.handle.ISendMsgHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsSendMsgHandler implements ISendMsgHandler {

    @Override
    public void sendMsg(String receiver, String title, String content) {
        // TODO 实现短信API
        log.info("短信发送：标题：{}", title);
        log.info("短信发送：接收人：{}", receiver);
        log.info("短信发送：正文：{}", content);
    }
}
