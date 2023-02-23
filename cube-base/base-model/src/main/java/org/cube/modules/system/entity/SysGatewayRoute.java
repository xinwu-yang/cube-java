package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Gateway路由管理
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2020-09-27
 */
@Data
public class SysGatewayRoute implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 路由ID
     */
    private String routerId;

    /**
     * 服务名
     */
    private String name;

    /**
     * 服务地址
     */
    private String uri;

    /**
     * 断言配置
     */
    private String predicates;

    /**
     * 过滤配置
     */
    private String filters;

    /**
     * 忽略前缀
     */
    @Dict("yn")
    private Integer stripPrefix;

    /**
     * 是否重试（0否、1是）
     */
    @Dict("yn")
    private Integer retryable;

    /**
     * 保留数据（0否、1是）
     */
    @Dict("yn")
    private Integer persist;

    /**
     * 在接口文档中展示（0否、1是）
     */
    @Dict("yn")
    private Integer showApi;

    /**
     * 状态（0无效、1有效）
     */
    @Dict("yn")
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateTime;

    /**
     * 所属部门
     */
    private String sysOrgCode;
}
