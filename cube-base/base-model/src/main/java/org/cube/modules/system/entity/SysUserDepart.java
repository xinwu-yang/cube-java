package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_user_depart")
@AllArgsConstructor
public class SysUserDepart implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 部门id
     */
    private String depId;

    public SysUserDepart(String id, String departId) {
        this.userId = id;
        this.depId = departId;
    }
}
