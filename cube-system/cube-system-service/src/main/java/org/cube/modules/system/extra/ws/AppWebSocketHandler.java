package org.cube.modules.system.extra.ws;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.cube.commons.constant.WebSocketConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket消息处理
 *
 * @author scott
 * @since 2019/11/29 9:41
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}") //此注解相当于设置访问URL
public class AppWebSocketHandler {

    private static final CopyOnWriteArraySet<AppWebSocketHandler> WEB_SOCKET_HANDLERS = new CopyOnWriteArraySet<>();
    private static final Map<String, Session> SESSION_POOL = new HashMap<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        this.session = session;
        WEB_SOCKET_HANDLERS.add(this);
        SESSION_POOL.put(userId, session);
        log.info("【WebSocket消息】有新的连接，总数为:" + WEB_SOCKET_HANDLERS.size());
    }

    @OnClose
    public void onClose() {
        WEB_SOCKET_HANDLERS.remove(this);
        log.info("【WebSocket消息】连接断开，总数为:" + WEB_SOCKET_HANDLERS.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.debug("【WebSocket消息】收到客户端消息:" + message);
        JSONObject obj = JSONUtil.createObj();
        obj.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_CHECK);//业务类型
        obj.set(WebSocketConst.MSG_TXT, "心跳响应");//消息内容
        session.getAsyncRemote().sendText(obj.toString());
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("【WebSocket消息】广播消息:" + message);
        for (AppWebSocketHandler webSocket : WEB_SOCKET_HANDLERS) {
            try {
                if (webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        Session session = SESSION_POOL.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("【WebSocket消息】单点消息:" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = SESSION_POOL.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【WebSocket消息】单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}