package org.cube.cloud.starter.redis.listener;


import org.cube.commons.base.BaseMap;

/**
 * 自定义消息监听
 */
public interface CubeRedisListener {

    /**
     * 接受消息
     *
     * @param message 消息内容
     */
    void onMessage(BaseMap message);
}
