package org.cube.cloud.starter.lock.aspect;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义了从EL表达式获取数据
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-04-11
 */
@Slf4j
public class BaseAspect {

    /**
     * 通过spring SpEL 获取参数
     *
     * @param key            定义的key值 以#开头 例如:#user
     * @param parameterNames 形参
     * @param values         形参值
     * @param keyConstant    key的常亮
     */
    public List<String> getValueBySpEL(String key, String[] parameterNames, Object[] values, String keyConstant) {
        if (!key.contains("#")) {
            String redisKey = getKey(key, keyConstant);
            log.info("lockKey：{}", redisKey);
            return ListUtil.of(redisKey);
        }
        List<String> keys = new ArrayList<>();
        //spel解析器
        ExpressionParser parser = new SpelExpressionParser();
        //spel上下文
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], values[i]);
        }
        Expression expression = parser.parseExpression(key);
        Object value = expression.getValue(context);
        if (value != null) {
            if (value instanceof List) {
                List value1 = (List) value;
                for (Object o : value1) {
                    keys.add(getKey(o, keyConstant));
                }
            } else if (value.getClass().isArray()) {
                Object[] obj = (Object[]) value;
                for (Object o : obj) {
                    keys.add(getKey(o, keyConstant));
                }
            } else {
                keys.add(getKey(value, keyConstant));
            }
        }
        log.info("表达式：key[{}], value[{}]", key, keys);
        return keys;
    }

    private String getKey(Object o, String keyConstant) {
        return "redis:lock:" + keyConstant + ":" + o.toString();
    }
}
