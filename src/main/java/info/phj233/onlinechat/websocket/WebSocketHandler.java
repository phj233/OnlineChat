package info.phj233.onlinechat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.phj233.onlinechat.websocket.entity.MessageTemplate;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
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
    private final Set<String> sessionIds = new CopyOnWriteArraySet<>();
    /**
     * websocket客户端链接成功的时候触发
     * @param session 会话
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        log.info("websocket连接成功"+session);
        sessions.add(session);
        sessionIds.add(session.getId());
        MessageTemplate messageTemplate = new MessageTemplate(3, "欢迎加入聊天室",sessionIds,sessions.size());
        broadcast(messageTemplate);
    }

    /**
     * 和客户端建立连接后，处理客户端发送的请求
     * @param session 会话
     * @param message 消息
     */
    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) {
        log.info(session.getId()+"发送消息："+message.getPayload());
        String payload = message.getPayload();
        try {
            MessageTemplate messageTemplate = objectMapper.readValue(payload, MessageTemplate.class);
            switch (messageTemplate.getType()) {
                case 1:
                    // 私发消息
                    Set<String> receivers = messageTemplate.getReceivers();
                    sendPrivateMessage(messageTemplate, receivers.iterator().next(), session);
                    break;
                case 2:
                    // 群发消息
                    broadcast(messageTemplate, session);
                    break;
                case 3:
                    // 广播消息
                    broadcast(messageTemplate);
                default:
                    break;
            }
        }catch (Exception e){
            log.error("消息解析失败", e);
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
        broadcast(new MessageTemplate(3, "用户离开聊天室", sessionIds, sessions.size()));
    }

    /**
     * websocket连接异常时调用
     * @param session 会话
     * @param exception 异常
     */
    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        log.error("websocket连接异常");

    }

    /**
     * 广播消息
     * @param message 消息
     */
    private void broadcast(MessageTemplate message) {
        for (WebSocketSession session : sessions) {
            try {
                synchronized (sessions) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                }
            } catch (IOException e) {
                log.error("发送广播消息失败", e);
            }
        }
    }

    /**
     * 广播消息，发送者除外
     * @param messageTemplate 消息
     * @param sender 发送者
     */
    private void broadcast(MessageTemplate messageTemplate, WebSocketSession sender) {
        for (WebSocketSession session : sessions) {
            if (session.getId().equals(sender.getId())) {
                continue;
            }
            try {
                synchronized (sessions) {
                    String message = objectMapper.writeValueAsString(messageTemplate);
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                log.error("发送广播消息失败", e);
            }
        }
    }

    /**
     * 私发消息
     * @param messageTemplate 消息
     * @param receiver 接收者
     * @param sender 发送者
     */
    private void sendPrivateMessage(MessageTemplate messageTemplate, String receiver, WebSocketSession sender) {
        WebSocketSession receiverSession = findSessionById(receiver);
        try {
            if (!ObjectUtils.isEmpty(receiverSession)) {
                String message = objectMapper.writeValueAsString(messageTemplate);
                receiverSession.sendMessage(new TextMessage(message));
            }else {
                log.error("发送私发消息失败，接收者不存在");
                sender.sendMessage(new TextMessage(objectMapper.writeValueAsString(new MessageTemplate(3, "发送私发消息失败，接收者不存在", sessionIds, sessions.size()))));
            }
        } catch (IOException e) {
            log.error("发送私发消息失败", e);
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