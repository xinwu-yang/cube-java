package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户角色表
 *
 * @author 杨欣武
 * @since 2021-09-27
 */
@Data
@NoArgsConstructor
public class SysUserRole implements Serializable {
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
     * 角色id
     */
    private String roleId;

    public SysUserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
