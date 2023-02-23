package org.cube.commons.intf;

import cn.hutool.json.JSONObject;

/**
 * 填值规则接口
 *
 * @author 杨欣武
 * @apiNote 如需使用填值规则功能，规则实现类必须实现此接口
 */
public interface IFillRuleHandler {

    /**
     * @param params   页面配置固定参数
     * @param formData 动态表单参数
     */
    Object execute(JSONObject params, String formData);
}

