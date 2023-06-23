package info.phj233.onlinechat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.util.result.E;
import info.phj233.onlinechat.util.result.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 退出成功处理类
 * @author phj233
 * @since  2023/3/11 12:00
 * @version 1.0
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    /**
     * 用户退出返回结果，同时让前端清除token
     */
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null ){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        SecurityContextHolder.clearContext();
        response.setHeader(JWTConfig.tokenHeader, "");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        R.ok(E.SUCCESS)
                )
        );

    }
}
