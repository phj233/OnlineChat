package top.phj233.backend_sb3.ws.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import top.phj233.backend_sb3.ws.interceptor.HandshakeHandler
import top.phj233.backend_sb3.ws.interceptor.HandshakeInterceptor

/**
 * WebSocket 配置
 * 建立连接路径/ws
 * 订阅路径/topic
 * 接收前端消息路径/app @MessageMapping 就是这个路径
 */

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig: WebSocketMessageBrokerConfigurer {
    /**
     * 注册 STOMP 端点
     * @param registry STOMP 端点注册表
     * @see StompEndpointRegistry
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/chat")
            .addInterceptors(HandshakeInterceptor())
            .setHandshakeHandler(HandshakeHandler())
            .setAllowedOriginPatterns("*")
    }

    /**
     * 配置消息代理
     * /topic 用于广播 /queue 用于点对点
     * @param registry 消息代理注册表
     * @see MessageBrokerRegistry
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        //客户端订阅路径 ,/topic /queue可向客户端发送消息
        registry.enableSimpleBroker("/topic", "/queue")
        //客户端发送消息路径
        registry.setApplicationDestinationPrefixes("/app")
        //用户订阅路径 发送消息给指定用户
        registry.setUserDestinationPrefix("/user")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors()
    }
}
