package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志表
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2018-12-26
 */
@Data
public class SysLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 耗时
     */
    private Long costTime;

    /**
     * IP
     */
    private String ip;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 操作人用户名
     */
    private String username;

    /**
     * 操作人用户账户
     */
    private String userid;

    /**
     * 操作详细日志
     */
    private String logContent;

    /**
     * 日志类型（1登录日志、2操作日志）
     */
    @Dict("log_type")
    private Integer logType;

    /**
     * 操作类型（1查询，2添加，3修改，4删除，5导入，6导出）
     */
    @Dict("operate_type")
    private Integer operateType;
}
