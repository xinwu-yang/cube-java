package org.cube.modules.system.enhancement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户菜单点击历史记录
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2021-09-27
 */
@Data
@TableName("sys_permission_history")
public class SysPermissionHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /**
     * 菜单id
     */
    @Dict(table = "sys_permission", value = "id", text = "name")
    private String permissionId;

    /**
     * 用户id
     */
    @Dict(table = "sys_user", value = "id", text = "username")
    private String userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
