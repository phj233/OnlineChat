package info.phj233.onlinechat.websocket.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.phj233.onlinechat.websocket.entity.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * MessageTemplate 序列化器
 * @see MessageTemplate
 * @author phj233
 * @since 2023/6/23 14:16
 */
@JsonComponent
@Slf4j
public class MessageTemplateJsonComponent extends JsonSerializer<MessageTemplate> {
    @Override
    public void serialize(MessageTemplate value, JsonGenerator gen, SerializerProvider serializers){
        try {
            gen.writeStartObject();
            gen.writeNumberField("type", value.getType());
            gen.writeStringField("message", value.getMessage());
            gen.writeArrayFieldStart("receivers");
            value.getReceivers().forEach((receiver) -> {
                try {
                    gen.writeString(receiver);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            gen.writeEndArray();
            gen.writeNumberField("onlineCount", value.getOnlineCount());
            gen.writeEndObject();
        }catch (IOException e){
            log.error("MessageTemplate 序列化失败{}", e.getMessage());
        }

    }
}
