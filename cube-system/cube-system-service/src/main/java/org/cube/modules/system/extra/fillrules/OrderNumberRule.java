package org.cube.modules.system.extra.fillrules;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.cube.commons.intf.IFillRuleHandler;

import java.util.Date;

/**
 * 填值规则Demo：生成订单号
 */
public class OrderNumberRule implements IFillRuleHandler {

    @SneakyThrows
    @Override
    public Object execute(JSONObject params, String formData) {
        String prefix = "No.";
        //订单前缀默认为CN 如果规则参数不为空，则取自定义前缀
        if (params != null) {
            prefix = params.getStr("prefix");
        }
        int random = RandomUtil.randomInt(0, 90) + 10;
        String value = prefix + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + random;
        String name = JSONUtil.parseObj(formData).getStr("name");
        if (StrUtil.isNotEmpty(name)) {
            value += name;
        }
        return value;
    }
}