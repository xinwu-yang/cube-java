package org.cube.commons.model;

import org.cube.modules.system.model.LoginUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志对象
 * cloud api 用到的接口传输对象
 */
@Data
@NoArgsConstructor
public class LogDTO implements Serializable {

    /**
     * 内容
     */
    private String logContent;

    /**
     * 日志类型(1:登录日志;2:操作日志)
     */
    private Integer logType;

    /**
     * 操作类型（1查询，2添加，3修改，4删除，5导入，6导出）
     */
    private Integer operateType;

    /**
     * 登录用户
     */
    private LoginUser loginUser;

    /**
     * 日志id
     */
    private String id;

    /**
     * 日志记录时间
     */
    private Date createTime;

    /**
     * 操作花费的时间
     */
    private Long costTime;

    /**
     * 登录、操作来源的IP地址
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
     * 操作人用户名称
     */
    private String username;

    /**
     * 操作人用户账户
     */
    private String userid;

    public LogDTO(String logContent, Integer logType, Integer operatetype) {
        this.logContent = logContent;
        this.logType = logType;
        this.operateType = operatetype;
    }

    public LogDTO(String logContent, Integer logType, Integer operatetype, LoginUser loginUser) {
        this.logContent = logContent;
        this.logType = logType;
        this.operateType = operatetype;
        this.loginUser = loginUser;
    }
}
