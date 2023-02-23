package org.cube.modules.system.sms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.commons.base.CubeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息模板
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_sms_template")
public class SysMessageTemplate extends CubeEntity {

    /**
     * 模板CODE
     */
    private String templateCode;

    /**
     * 模板标题
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 模板测试json
     */
    private String templateTestJson;

    /**
     * 模板类型
     */
    private String templateType;
}
