package info.phj233.onlinechat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.phj233.onlinechat.websocket.entity.MessageTemplate;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * websocket处理器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /**
     * websocket客户端链接成功的时候触发
     * @param session 会话
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        log.info("websocket连接成功"+session);
        sessions.add(session);
        MessageTemplate messageTemplate = new MessageTemplate(3, "用户加入聊天室", sessions, sessions.size());
        broadcast(messageTemplate.toString());

    }

    /**
     * 和客户端建立连接后，处理客户端发送的请求
     * @param session 会话
     * @param message 消息
     */
    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) {
        log.info("收到消息: {}", message.getPayload());
        String payload = message.getPayload();
        MessageTemplate messageTemplate = objectMapper.convertValue(payload, MessageTemplate.class);
        switch (messageTemplate.getType()) {
            case 1:
                // 私发消息
                break;
            case 2:
                // 群发消息
                broadcast(payload);
                break;
            default:
                break;
        }


    }

    /**
     * 和客户端建立连接后，处理客户端发送的二进制请求
     * @param session 会话
     * @param message 消息
     */
    @Override
    public void handleBinaryMessage(@NotNull WebSocketSession session,@NotNull BinaryMessage message) {
        log.info("收到消息: {}", message);
    }

    /**
     * 和客户端断开连接的时候触发该方法
     * @param session 会话
     * @param status 状态
     */
    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        String reason = status.getReason();
        log.info("websocket连接关闭，原因：{}", reason);
        broadcast(new MessageTemplate(2, "用户离开聊天室", sessions, sessions.size()).toString());
    }

    /**
     * websocket连接异常时调用
     * @param session 会话
     * @param exception 异常
     */
    @Override
    public void handleTransportError( @NotNull WebSocketSession session, @NotNull Throwable exception) {
        log.error("websocket连接异常");

    }

    /**
     * 广播消息
     * @param message 消息
     */
    private void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("发送广播消息失败", e);
            }
        }
    }

    /**
     * 广播消息，某些用户除外
     * @param message 消息
     * @param receivers 接收者
     */
    private void broadcast(String message, Set<WebSocketSession> receivers) {
        for (WebSocketSession session : sessions) {
            if (!receivers.contains(session)) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("发送广播消息失败", e);
                }
            }
        }
    }


    /**
     * 查找会话对象
     * @param id 会话ID
     * @return 会话对象，如果不存在则返回null
     */
    private WebSocketSession findSessionById(String id) {
        for (WebSocketSession session : sessions) {
            if (session.getId().equals(id)) {
                return session;
            }
        }
        return null;
    }
}