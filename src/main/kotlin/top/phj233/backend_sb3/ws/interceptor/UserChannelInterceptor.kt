package top.phj233.backend_sb3.ws.interceptor

import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.ChannelInterceptor

/**
 * 用户频道拦截器
 * @author phj233
 * @since 2024/10/9 17:07
 * @version
 */
class UserChannelInterceptor : ChannelInterceptor {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        log.info("preSend: $message")
        return super.preSend(message, channel)
    }
}
