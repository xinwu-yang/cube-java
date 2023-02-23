package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户信息
 */
@Data
@TableName("sys_tenant")
public class SysTenant implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 状态 1正常 0冻结
     */
    @Dict("tenant_status")
    private Integer status;
}
