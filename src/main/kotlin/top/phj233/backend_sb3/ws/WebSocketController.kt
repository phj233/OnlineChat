package top.phj233.backend_sb3.ws

import org.babyfish.jimmer.spring.model.SortUtils
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import top.phj233.backend_sb3.ws.dto.WsMessage
import top.phj233.backend_sb3.ws.dto.WsMessageView

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@RestController
class WebSocketController(
    val simpMessageTemplate: SimpMessagingTemplate,
    val messageRepository: MessageRepository
    ) {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 发送消息给所有订阅了 /topic/public 的用户
     */
    @MessageMapping("/public")
    @SendTo("/topic/public")
    fun sendPublicMessage(@Payload message: WsMessage): WsMessageView? {
        log.info("sendPublicMessage: $message")
        val id = messageRepository.save(message, SaveMode.INSERT_ONLY).modifiedEntity.id
        val messageView = messageRepository.viewer(WsMessageView::class).findNullable(id)
        return messageView
    }

    /**
     * 发送消息给指定用户
     */
    @MessageMapping("/private")
    fun sendPrivateMessage(userId: String,message: String) {
        simpMessageTemplate.convertAndSendToUser(userId, "/topic/private", message)
    }

    @GetMapping("/ws/msg/all")
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): Page<WsMessageView> {
        val pageRequest = PageRequest.of(
            page,
            size,
            SortUtils.toSort("time")
        )
        return messageRepository.viewer(WsMessageView::class).findAll(pageRequest)
    }
}
