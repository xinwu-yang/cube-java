package org.cube.modules.system.enhancement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cube.codegen.annotations.*;
import org.cube.codegen.annotations.db.ForcedSync;
import org.cube.codegen.annotations.models.ComponentType;
import org.cube.codegen.core.models.ComponentParamKey;
import org.cube.commons.annotations.Dict;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问密钥即AK/SK
 */
@Data
@ForcedSync
@NoArgsConstructor
@AllArgsConstructor
@Form(description = "访问密钥即AK/SK")
@QueryFields({@QueryField(label = "用户名", value = "username", component = ComponentType.STRING)})
public class SysRoleKey implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    @FormField(title = "用户名", component = ComponentType.SELECT_MULTI_USER)
    private String username;

    /**
     * Access Key ID
     */
    @FormField(title = "AK", showInForm = false)
    private String accessKey;

    /**
     * Secret Access Key
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FormField(title = "SK", showInForm = false, showInList = false)
    private String secretKey;

    /**
     * 是否启用（0禁用、1启用）
     */
    @FormField(title = "是否启用", component = ComponentType.DICT_SELECT_TAG, componentParams = {@ComponentParam(key = ComponentParamKey.DICT_CODE, value = "dict_item_status")})
    @Dict("dict_item_status")
    private Integer enabled;

    /**
     * 创建时间
     */
    @FormField(title = "创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @FormField(title = "创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @FormField(title = "更新时间")
    private Date updateTime;

    /**
     * 更新人
     */
    @FormField(title = "更新人")
    private String updateBy;
}
