package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门角色人员信息
 *
 * @author xinwuy
 * @version V1.0
 * @since 2020-02-13
 */
@Data
public class SysDepartRoleUser implements Serializable {

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
    private String droleId;

    public SysDepartRoleUser(String userId, String droleId) {
        this.userId = userId;
        this.droleId = droleId;
    }
}
