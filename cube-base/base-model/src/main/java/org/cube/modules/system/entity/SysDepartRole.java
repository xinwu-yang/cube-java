package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门角色
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-02-12
 */
@Data
public class SysDepartRole implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     *
     */
    @Dict(table = "sys_depart", text = "depart_name", value = "id")
    private String departId;

    /**
     * 部门角色名称
     */
    private String roleName;

    /**
     * 部门角色编码
     */
    private String roleCode;

    /**
     * 描述
     */
    private String description;

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
