package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 多数据源管理
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-12-25
 */
@Data
public class SysDataSource implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 数据源编码
     */
    private String code;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据库类型
     */
    @Dict("database_type")
    private String dbType;

    /**
     * 驱动类
     */
    private String dbDriver;

    /**
     * 数据源地址
     */
    private String dbUrl;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 用户名
     */
    private String dbUsername;

    /**
     * 密码
     */
    private String dbPassword;

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
