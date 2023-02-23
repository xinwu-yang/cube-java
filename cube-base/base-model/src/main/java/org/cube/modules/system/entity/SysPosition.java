package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;

/**
 * 职务表
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2021-09-27
 */
@Data
public class SysPosition implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 职务编码
     */
    private String code;

    /**
     * 职务名称
     */
    private String name;

    /**
     * 职级
     */
    @Dict("position_rank")
    private String postRank;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private java.util.Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private java.util.Date updateTime;

    /**
     * 组织机构编码
     */
    private String sysOrgCode;
}
