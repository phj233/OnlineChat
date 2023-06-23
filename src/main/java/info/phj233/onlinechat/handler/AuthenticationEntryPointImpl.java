package info.phj233.onlinechat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.phj233.onlinechat.util.result.E;
import info.phj233.onlinechat.util.result.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用户未登录处理类
 * @author phj233
 * @since  2023/3/10 22:44
 * @version: 1.0
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        R.error(E.USER_NOT_LOGIN)
                )
        );
    }
}
