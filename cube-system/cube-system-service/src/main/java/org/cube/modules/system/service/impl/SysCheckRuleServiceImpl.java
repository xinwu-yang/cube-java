package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.mapper.SysCheckRuleMapper;
import org.cube.modules.system.service.ISysCheckRuleService;
import lombok.SneakyThrows;
import org.cube.modules.system.entity.SysCheckRule;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class SysCheckRuleServiceImpl extends ServiceImpl<SysCheckRuleMapper, SysCheckRule> implements ISysCheckRuleService {

    /**
     * 位数特殊符号，用于检查整个值，而不是裁剪某一段
     */
    private static final String CHECK_ALL_SYMBOL = "*";

    @Override
    public SysCheckRule getByCode(String ruleCode) {
        LambdaQueryWrapper<SysCheckRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCheckRule::getRuleCode, ruleCode);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过用户设定的自定义校验规则校验传入的值
     *
     * @return 返回 null代表通过校验，否则就是返回的错误提示文本
     */
    @Override
    @SneakyThrows
    public JSONObject checkValue(SysCheckRule checkRule, String value) {
        if (checkRule != null && StrUtil.isNotBlank(value)) {
            String ruleJson = checkRule.getRuleJson();
            if (StrUtil.isNotBlank(ruleJson)) {
                // 开始截取的下标，根据规则的顺序递增，但是 * 号不计入递增范围
                int beginIndex = 0;
                JSONArray rules = JSONUtil.parseArray(ruleJson);
                for (int i = 0; i < rules.size(); i++) {
                    JSONObject result = JSONUtil.createObj();
                    JSONObject rule = rules.getJSONObject(i);
                    // 位数
                    String digits = rule.getStr("digits");
                    result.set("digits", digits);
                    // 验证规则
                    String pattern = rule.getStr("pattern");
                    result.set("pattern", pattern);
                    // 未通过时的提示文本
                    String message = rule.getStr("message");
                    result.set("message", message);
                    // 根据用户设定的区间，截取字符串进行验证
                    String checkValue;
                    // 是否检查整个值而不截取
                    if (CHECK_ALL_SYMBOL.equals(digits)) {
                        checkValue = value;
                    } else {
                        int num = Integer.parseInt(digits);
                        int endIndex = beginIndex + num;
                        // 如果结束下标大于给定的值的长度，则取到最后一位
                        endIndex = Math.min(endIndex, value.length());
                        // 如果开始下标大于结束下标，则说明用户还尚未输入到该位置，直接赋空值
                        if (beginIndex > endIndex) {
                            checkValue = "";
                        } else {
                            checkValue = value.substring(beginIndex, endIndex);
                        }
                        result.set("beginIndex", beginIndex);
                        result.set("endIndex", endIndex);
                        beginIndex += num;
                    }
                    result.set("checkValue", checkValue);
                    boolean passed = Pattern.matches(pattern, checkValue);
                    result.set("passed", passed);
                    // 如果没有通过校验就返回错误信息
                    if (!passed) {
                        return result;
                    }
                }
            }
        }
        return null;
    }
}
