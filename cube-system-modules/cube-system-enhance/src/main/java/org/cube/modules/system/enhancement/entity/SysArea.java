package org.cube.modules.system.enhancement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.commons.annotations.Dict;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 地区管理
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2021-09-27
 */
@Data
@NoArgsConstructor
@TableName("sys_area")
public class SysArea implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 父Id
     */
    @Dict(table = "sys_area", value = "id", text = "name")
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 国标编码
     */
    private String gbCode;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 菜单等级
     */
    @Dict("area_code")
    private Integer level;

    /**
     * 全路径
     */
    private String path;

    /**
     * 是否有子节点
     */
    private Boolean hasChildren;

    /**
     * 删除状态（0，正常，1已删除）
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建日期
     */
    private Date createTime;
}
