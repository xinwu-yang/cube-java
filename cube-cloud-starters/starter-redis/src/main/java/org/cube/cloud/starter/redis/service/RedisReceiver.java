package org.cube.cloud.starter.redis.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.cube.commons.base.BaseMap;
import org.cube.cloud.starter.redis.listener.CubeRedisListener;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RedisReceiver {

    /**
     * 接受消息并调用业务逻辑处理器
     */
    public void onMessage(BaseMap params) {
        Object handlerName = params.get("handlerName");
        CubeRedisListener messageListener = SpringUtil.getBean(handlerName.toString(), CubeRedisListener.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }
}
