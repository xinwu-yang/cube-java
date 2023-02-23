package org.cube.modules.system.sms.handle;

/**
 * 发送消息
 */
public interface ISendMsgHandler {

    /**
     * 发送消息
     *
     * @param receiver 接收者
     * @param title    标题
     * @param content  内容
     */
    void sendMsg(String receiver, String title, String content);
}
