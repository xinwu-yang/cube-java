package org.cube.commons.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.intf.IFillRuleHandler;

/**
 * 规则值自动生成工具类
 * 例如：自动生成订单号；自动生成当前日期
 *
 * @author qinfeng
 * @version 2.0.0
 * @since 2022-02-08
 */
@Slf4j
public class FillRuleUtil {

    @SneakyThrows
    public static Object executeRule(String ruleCode, String formData) {
        if (StrUtil.isEmpty(ruleCode)) {
            return null;
        }
        // 获取 Service
        ServiceImpl impl = SpringUtil.getBean("sysFillRuleServiceImpl");
        // 根据 ruleCode 查询出实体
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rule_code", ruleCode);
        JSONObject root = JSONUtil.parseObj(impl.getOne(queryWrapper));
        if (root.isEmpty()) {
            log.warn("填值规则：" + ruleCode + " 不存在！");
            return null;
        }
        // 获取必要的参数
        String ruleClass = root.getStr("ruleClass");
        JSONObject params = root.getJSONObject("ruleParams");
        if (params.size() == 0) {
            params = JSONUtil.createObj();
        }
        // 通过反射执行配置的类里的方法
        IFillRuleHandler ruleHandler = (IFillRuleHandler) Class.forName(ruleClass).getDeclaredConstructor().newInstance();
        return ruleHandler.execute(params, formData);
    }
}
