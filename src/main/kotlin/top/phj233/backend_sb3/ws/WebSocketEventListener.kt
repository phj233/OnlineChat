package top.phj233.backend_sb3.ws

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent

/**
 * @author phj233
 * @since 2024/10/5 15:15
 * @version
 */
@Component
class WebSocketEventListener(
    private val simpMessagingTemplate: SimpMessageSendingOperations,

) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @EventListener
    fun handleConnectEvent(event: SessionConnectEvent) {
        log.info("handleConnectEvent: {}", event)
        simpMessagingTemplate.convertAndSend("/topic/public", "用户${event.user?.name}连接")
    }

    @EventListener
    fun handleDisconnectEvent(event: SessionDisconnectEvent) {
        log.info("handleDisconnectEvent: {}", event)
        simpMessagingTemplate.convertAndSend("/topic/public", "用户${event.user?.name}断开连接")
    }

    @EventListener
     fun handleSubscribeEvent(event: SessionSubscribeEvent) {
        log.info("handleSubscribeEvent: {}", event)
        simpMessagingTemplate.convertAndSend("/topic/public", "用户${event.user?.name}订阅")
     }
}
