package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分类字典
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2019-05-29
 */
@Data
public class SysCategory implements Serializable, Comparable<SysCategory> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @Schema(title = "主键")
    private Long id;

    /**
     * 父级节点
     */
    @Schema(title = "父节点id")
    private Long pid;

    /**
     * 类型名称
     */
    @Schema(title = "类型名称")
    private String name;

    /**
     * 类型编码
     */
    @Schema(title = "类型编码")
    private String code;

    /**
     * 创建人
     */
    @Schema(title = "创建人")
    private String createBy;

    /**
     * 创建日期
     */
    @Schema(title = "创建日期")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(title = "更新人")
    private String updateBy;

    /**
     * 更新日期
     */
    @Schema(title = "更新日期")
    private Date updateTime;

    /**
     * 所属部门
     */
    @Schema(title = "所属部门")
    private String sysOrgCode;

    /**
     * 是否有子节点
     */
    @Schema(title = "是否有子节点", description = "0:无、1:有")
    private String hasChild;


    @Override
    public int compareTo(SysCategory o) {
        // 比较条件我们定的是按照code的长度升序
        // <0：当前对象比传入对象小。
        // =0：当前对象等于传入对象。
        // >0：当前对象比传入对象大。
        return this.code.length() - o.code.length();
    }
}
