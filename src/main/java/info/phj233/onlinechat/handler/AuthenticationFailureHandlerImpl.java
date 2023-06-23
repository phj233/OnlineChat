package info.phj233.onlinechat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.phj233.onlinechat.util.result.E;
import info.phj233.onlinechat.util.result.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登录失败处理类
 * @author phj233
 * @since  2023/3/10 22:46
 * @version 1.0
 */
@Component
@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("UTF-8");
        switch (exception.getClass().getName()) {
            case "UsernameNotFoundException" -> {
                log.info("【登录失败】" + exception.getMessage());
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                R.error(E.USER_NOT_EXIST)
                        )
                );}
            case "BadCredentialsException" -> {
                log.info("【登录失败】" + exception.getMessage());
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                R.error(E.USERNAME_PASSWORD_ERROR)
                        )
                );
            }
            case "LockedException" -> {
                log.info("【登录失败】" + exception.getMessage());
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                R.error(E.USER_LOCKED)
                        )
                );
            }
            default -> {
                log.info("【登录失败】" + exception.getMessage());
                response.getWriter().write(
                        objectMapper.writeValueAsString(
                                R.error(E.ERROR, exception.getMessage()
                                )
                        )
                );
            }
        }
    }
}
