package org.cube.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 填值规则
 *
 * @author xinwuy
 * @version V2.3.0
 * @since 2019-11-07
 */
@Data
public class SysFillRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则Code
     */
    private String ruleCode;

    /**
     * 规则实现类
     */
    private String ruleClass;

    /**
     * 规则参数
     */
    private String ruleParams;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;
}
