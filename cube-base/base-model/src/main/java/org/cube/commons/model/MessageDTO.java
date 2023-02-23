package org.cube.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 普通消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {

    /**
     * 发送人(用户登录账户)
     */
    private String fromUser;

    /**
     * 接收人(用户登录账户)
     */
    private String toUser;

    /**
     * 消息主题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型 1:消息  2:系统消息
     */
    private Integer category;

    /**
     * 构造器1 系统消息
     */
    public MessageDTO(String fromUser, String toUser, String title, String content) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.title = title;
        this.content = content;
        //默认 都是2系统消息
        this.category = 2;
    }
}
