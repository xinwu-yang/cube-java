package org.cube.modules.system.sms.handle.enums;

import cn.hutool.core.util.StrUtil;

/**
 * 发送消息类型枚举
 */
public enum SendMsgTypeEnum {

    //推送方式：1短信 2邮件
    SMS("1", "org.jeecg.modules.message.handle.impl.SmsSendMsgHandle"),
    EMAIL("2", "org.jeecg.modules.message.handle.impl.EmailSendMsgHandle");

    private String type;

    private String implClass;

    private SendMsgTypeEnum(String type, String implClass) {
        this.type = type;
        this.implClass = implClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public static SendMsgTypeEnum getByType(String type) {
        if (StrUtil.isEmpty(type)) {
            return null;
        }
        for (SendMsgTypeEnum val : values()) {
            if (val.getType().equals(type)) {
                return val;
            }
        }
        return null;
    }
}
