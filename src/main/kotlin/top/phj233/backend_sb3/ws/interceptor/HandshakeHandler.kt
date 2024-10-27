package top.phj233.backend_sb3.ws.interceptor

import cn.dev33.satoken.stp.StpUtil
import org.springframework.http.server.ServerHttpRequest
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

/**
 * 握手处理器
 * @author phj233
 * @since 2024/10/9 20:48
 * @version
 */
class HandshakeHandler:DefaultHandshakeHandler() {
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal {
        val email = StpUtil.getLoginIdAsString()
        return User(email)
    }
}
class User(val email: String) :Principal {
    override fun getName(): String {
        return email
    }
}
