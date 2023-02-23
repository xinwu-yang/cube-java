package org.cube.cloud.starter.rabbitmq.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义网关刷新远程事件
 *
 * @author : 杨欣武
 * @since :2022-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CubeRemoteApplicationEvent extends RemoteApplicationEvent {

    private EventObj eventObj;

    public CubeRemoteApplicationEvent(EventObj source, String originService, String destinationService) {
        super(source, originService, DEFAULT_DESTINATION_FACTORY.getDestination(destinationService));
        this.eventObj = source;
    }

    public CubeRemoteApplicationEvent(EventObj source, String originService) {
        super(source, originService, DEFAULT_DESTINATION_FACTORY.getDestination(null));
        this.eventObj = source;
    }
}
