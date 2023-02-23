package org.cube.cloud.starter.rabbitmq.exchange;

import org.springframework.amqp.core.CustomExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟交换器构造器
 *
 * @author zyf
 * @version 2.0.0
 * @since 2019-03-08
 */
public class DelayExchangeBuilder {

    /**
     * 默认延迟消息交换器
     */
    public final static String DEFAULT_DELAY_EXCHANGE = "cube.delayed.exchange";
    /**
     * 普通交换器
     */
    public final static String DELAY_EXCHANGE = "cube.direct.exchange";

    /**
     * 构建延迟消息交换器
     */
    public static CustomExchange buildExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DEFAULT_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }
}
