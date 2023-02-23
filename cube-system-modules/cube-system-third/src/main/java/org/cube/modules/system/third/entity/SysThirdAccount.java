package org.cube.modules.system.third.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 第三方登录账号
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-11-17
 */
@Data
@TableName("sys_third_account")
public class SysThirdAccount implements Serializable {

    /**
     * 编号
     */
    @TableId
    private String id;

    /**
     * 第三方登录id
     */
    private String sysUserId;

    /**
     * 登录来源
     */
    private String thirdType;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态(1-正常,2-冻结)
     */
    private Integer status;

    /**
     * 删除状态(0-正常,1-已删除)
     */
    private Integer delFlag;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 真实姓名
     */
    private String thirdUserUuid;
}
