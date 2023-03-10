package info.phj233.onlinechat.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 登录成功处理类
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.handler
 * @className: AuthenticationSuccessHandlerImpl
 * @author: phj233
 * @date: 2023/3/10 22:58
 * @version: 1.0
 */
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }
}
