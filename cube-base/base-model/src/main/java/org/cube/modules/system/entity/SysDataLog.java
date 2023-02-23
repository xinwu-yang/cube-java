package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据日志
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-02-04
 */
@Data
public class SysDataLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

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
     * 表名
     */
    private String dataTable;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 数据内容
     */
    private String dataContent;

    /**
     * 版本号
     */
    private String dataVersion;
}
