package org.cube.commons.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模板消息
 */
@Data
@NoArgsConstructor
public class TemplateMessageDTO implements Serializable {

    /**
     * 发送人(用户登录账户)
     */
    protected String fromUser;

    /**
     * 发送给(用户登录账户)
     */
    protected String toUser;

    /**
     * 消息主题
     */
    protected String title;

    /**
     * 消息内容
     */
    protected String content;

    /**
     * 构造器1 发模板消息用
     */
    public TemplateMessageDTO(String fromUser, String toUser, String title, String content) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.title = title;
        this.content = content;
    }
}
