package org.cube.modules.system.job.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务在线管理
 *
 * @author 杨欣武
 * @version 1.1.2
 * @since 2021-03-26
 */
@Data
@TableName("sys_quartz_job")
public class QuartzJob implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除状态
     */
    private Integer delFlag;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 任务类名
     */
    private String jobClassName;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 参数
     */
    private String parameter;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态 0正常 -1停止
     */
    @Dict("quartz_status")
    private Integer status;
}
