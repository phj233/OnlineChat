package info.phj233.onlinechat.websocket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

/**
 * 消息模板, 用于接收发送消息 <br>
 * type: 消息类型 1: 私发消息 2: 群发消息 3: 系统消息 <br>
 * message: 消息内容 <br>
 * receivers: 消息接收者 <br>
 * onlineCount: 在线人数 <br>
 * @author phj233
 * @since 2023/5/25 14:43
 */
@Data
@AllArgsConstructor
public class MessageTemplate {
    /**
     * 消息类型
     * 1: 私发消息
     * 2: 群发消息 除了自己
     * 3: 系统消息 上下线通知
     */
    Integer type;
    /**
     * 消息内容
     */
    String message;
    /**
     * 消息接收者
     */
    Set<WebSocketSession> receivers;
    /**
     * 在线人数
     */
    Integer onlineCount;
}
