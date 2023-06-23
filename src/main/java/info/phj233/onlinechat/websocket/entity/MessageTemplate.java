package info.phj233.onlinechat.websocket.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class MessageTemplate {
    /**
     * 消息类型
     * <br> 1: 私发消息
     * <br> 2: 群发消息 除了自己
     * <br> 3: 系统消息 上下线通知
     */
    Integer type;
    /**
     * 消息内容
     */
    String message;
    /**
     * 消息接收者
     */
    Set<String> receivers;
    /**
     * 在线人数
     */
    Integer onlineCount;

    @JsonCreator
    public MessageTemplate(@JsonProperty("type") int type,
                           @JsonProperty("message") String message,
                           @JsonProperty("receivers") Set<String> receivers,
                           @JsonProperty("onlineCount") int onlineCount) {

        this.type = type;
        this.message = message;
        this.receivers = receivers;
        this.onlineCount = onlineCount;
    }
}
