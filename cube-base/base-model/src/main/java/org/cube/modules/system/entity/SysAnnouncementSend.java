package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户通告阅读标记表
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-02-21
 */
@Data
public class SysAnnouncementSend implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 通告id
     */
    private Long anntId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 阅读状态（0未读，1已读）
     */
    private Integer readFlag;

    /**
     * 阅读时间
     */
    private Date readTime;

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
