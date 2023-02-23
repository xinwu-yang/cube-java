package org.cube.modules.system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 用户登录信息
 *
 * @author 杨欣武
 * @since 2022-06-27
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LoginUser {

    /**
     * 登录人id
     */
    private String id;

    /**
     * 登录人账号
     */
    private String username;

    /**
     * 登录人名字
     */
    private String realname;

    /**
     * 登录人密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 当前登录部门code
     */
    private String orgCode;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    private Integer sex;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 状态(1：正常 2：冻结 ）
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 身份（1 普通员工 2 上级）
     */
    private Integer userIdentity;

    /**
     * 管理部门ids
     */
    private String departIds;

    /**
     * 职务，关联职务表
     */
    private String post;

    /**
     * 座机号
     */
    private String telephone;

    /**
     * 多租户id配置，编辑用户的时候设置
     */
    private String relTenantIds;

    /**
     *
     * 是否是首次登录
     */
    private Boolean firstLogin;

    /**
     * 用户角色信息
     */
    private List<String> roleIds;

    /**
     * 用户所属的部门
     */
    private String orgCodes;
}
