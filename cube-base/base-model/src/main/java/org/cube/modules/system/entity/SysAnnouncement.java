package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统通告
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-01-02
 */
@Data
public class SysAnnouncement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String msgContent;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 发布人
     */
    private String sender;

    /**
     * 优先级（L低，M中，H高）
     */
    @Dict("priority")
    private String priority;

    /**
     * 消息类型（1通知公告、2系统消息）
     */
    @Dict("msg_category")
    private Integer msgCategory;

    /**
     * 通告对象类型（USER:指定用户，ALL:全体用户）
     */
    @Dict("msg_type")
    private String msgType;

    /**
     * 发布状态（0未发布，1已发布，2已撤销）
     */
    @Dict("send_status")
    private Integer sendStatus;

    /**
     * 发布时间
     */
    private Date sendTime;

    /**
     * 撤销时间
     */
    private Date cancelTime;

    /**
     * 指定用户
     */
    private String userIds;

    /**
     * 业务类型(email邮件、bpm流程)
     */
    private String busType;

    /**
     * 业务id
     */
    private String busId;

    /**
     * 打开方式（组件：component 路由：url）
     */
    private String openType;

    /**
     * 组件/路由地址
     */
    private String openPage;

    /**
     * 摘要
     */
    private String msgAbstract;

    /**
     * 删除状态（0正常、1已删除）
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
