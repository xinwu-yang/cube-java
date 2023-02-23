package org.cube.modules.system.sms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.commons.base.CubeEntity;
import org.cube.commons.annotations.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 消息
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_sms")
public class SysMessage extends CubeEntity {

    /**
     * 推送内容
     */
    private String esContent;

    /**
     * 推送所需参数Json格式
     */
    private String esParam;

    /**
     * 接收人
     */
    private String esReceiver;

    /**
     * 推送失败原因
     */
    private String esResult;

    /**
     * 发送次数
     */
    private Integer esSendNum;

    /**
     * 推送状态 0未推送 1推送成功 2推送失败
     */
    @Dict("msgSendStatus")
    private String esSendStatus;

    /**
     * 推送时间
     */
    private Date esSendTime;

    /**
     * 消息标题
     */
    private String esTitle;

    /**
     * 推送方式：1短信 2邮件 3微信
     */
    @Dict("msgType")
    private String esType;

    /**
     * 备注
     */
    private String remark;
}
