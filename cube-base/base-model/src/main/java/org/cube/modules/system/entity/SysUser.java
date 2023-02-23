package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.cube.commons.annotations.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @author xinwuy
 * @since 2021-09-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 密码盐
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1男、2女）
     */
    @Dict("sex")
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 当前登录的部门编码
     */
    private String orgCode;

    /**
     * 当前登录的部门名称
     */
    private transient String orgCodeTxt;

    /**
     * 状态（1正常、2冻结）
     */
    @Dict("user_status")
    private Integer status;

    /**
     * 删除状态（0正常、1已删除）
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 工号
     */
    private String workNo;

    /**
     * 职务编码
     */
    @Dict(table = "sys_position", text = "name", value = "code")
    private String post;

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

    /**
     * 身份（0普通成员、1上级）
     */
    private Integer userIdentity;

    /**
     * 负责的部门（上级）
     */
    @Dict(table = "sys_depart", text = "depart_name", value = "id")
    private String departIds;

    /**
     * 多租户id
     */
    private String relTenantIds;

    /**
     * 是否是首次登录
     */
    private Boolean firstLogin;
}
