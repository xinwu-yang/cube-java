package org.cube.cloud.starter.rabbitmq.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听远程事件,并分发消息到业务模块消息处理器
 */
@Component
public class BaseApplicationEvent implements ApplicationListener<CubeRemoteApplicationEvent> {

    @Override
    public void onApplicationEvent(CubeRemoteApplicationEvent cubeRemoteApplicationEvent) {
        EventObj eventObj = cubeRemoteApplicationEvent.getEventObj();
        if (ObjectUtil.isNotEmpty(eventObj)) {
            //获取业务模块消息处理器
            BusEventHandler busEventHandler = SpringUtil.getBean(eventObj.getHandlerName(), BusEventHandler.class);
            if (ObjectUtil.isNotEmpty(busEventHandler)) {
                //通知业务模块
                busEventHandler.onMessage(eventObj);
            }
        }
    }
}
