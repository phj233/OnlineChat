package top.phj233.backend_sb3.ws.interceptor

import cn.dev33.satoken.stp.StpUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

/**
 * WebSocket 握手拦截器
 * 用于处理 WebSocket 握手时的逻辑
 * @see HandshakeInterceptor
 */
class HandshakeInterceptor : HandshakeInterceptor {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        log.info("WS: 握手开始")
        val login = StpUtil.isLogin()
        if (login) {
            log.info("WS: 用户 ${StpUtil.getLoginId()} 连接了")
            attributes["token"] = StpUtil.getTokenValue()
            attributes["userId"] = StpUtil.getLoginIdAsString()
            return true
        } else {
            request.headers["cookie"]?.let { list ->
                list.forEach {
                    if (it.startsWith("token=")) {
                        val token = it.substringAfter("token=")
                        log.info("WS: $token")
                        StpUtil.setTokenValue(token)
                        if (StpUtil.getLoginIdByToken(token) == null) {
                            log.info("WS: 未登录用户连接了")
                            return false
                        }
                        StpUtil.login(StpUtil.getLoginIdByToken(token).toString())
                        return true
                    }
                }
            }
            log.info("WS: 未登录用户连接了")
            return false
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: java.lang.Exception?
    ) {
        log.info("WS: 握手成功")

    }
}
