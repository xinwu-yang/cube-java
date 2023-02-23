package org.cube.modules.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComboModel implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文档管理 表单table默认选中
     */
    private boolean checked;

    /**
     * 文档管理 表单table 用户账号
     */
    private String username;

    /**
     * 文档管理 表单table 用户邮箱
     */
    private String email;

    /**
     * 文档管理 表单table 角色编码
     */
    private String roleCode;
}
