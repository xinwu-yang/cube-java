package org.cube.modules.system.enhancement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单点击次数
 */
@Data
@NoArgsConstructor
public class PermissionClickCount implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单id
     */
    private String permissionName;
    /**
     * 点击次数
     */
    private int clickCount;
    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}
