package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门权限表
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-02-11
 */
@Data
public class SysDepartPermission implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 部门id
     */
    private String departId;

    /**
     * 权限id
     */
    private String permissionId;

    /**
     * 数据规则id
     */
    private String dataRuleIds;

    public SysDepartPermission(String departId, String permissionId) {
        this.departId = departId;
        this.permissionId = permissionId;
    }
}
