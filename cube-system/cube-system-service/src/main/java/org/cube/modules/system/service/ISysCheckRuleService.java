package org.cube.modules.system.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.entity.SysCheckRule;

/**
 * 编码校验规则
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysCheckRuleService extends IService<SysCheckRule> {

    /**
     * 通过 code 获取规则
     */
    SysCheckRule getByCode(String ruleCode);

    /**
     * 通过用户设定的自定义校验规则校验传入的值
     *
     * @return 返回 null代表通过校验，否则就是返回的错误提示文本
     */
    JSONObject checkValue(SysCheckRule checkRule, String value);
}
