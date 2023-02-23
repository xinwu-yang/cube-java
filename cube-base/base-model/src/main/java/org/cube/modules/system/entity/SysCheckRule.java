package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 编码校验规则
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-02-04
 */
@Data
public class SysCheckRule {

    /**
     * 主键
     */
    @TableId
    @Schema(title = "主键")
    private String id;

    /**
     * 规则名称
     */
    @Schema(title = "规则名称")
    private String ruleName;

    /**
     * 规则编码
     */
    @Schema(title = "规则编码")
    private String ruleCode;

    /**
     * 规则JSON
     */
    @Schema(title = "规则JSON")
    private String ruleJson;

    /**
     * 规则描述
     */
    @Schema(title = "规则描述")
    private String ruleDescription;

    /**
     * 更新人
     */
    @Schema(title = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private Date updateTime;

    /**
     * 创建人
     */
    @Schema(title = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private Date createTime;
}
