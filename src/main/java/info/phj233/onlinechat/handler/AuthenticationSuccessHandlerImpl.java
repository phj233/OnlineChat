package info.phj233.onlinechat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.util.JWTUtil;
import info.phj233.onlinechat.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理类
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.handler
 * @className: AuthenticationSuccessHandlerImpl
 * @author: phj233
 * @date: 2023/3/10 22:58
 * @version: 1.0
 */
@Component
@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("UTF-8");
        UserDetailImpl user = (UserDetailImpl) authentication.getPrincipal();
        String token = JWTUtil.generateToken(user.getUser());
        token = JWTConfig.tokenPrefix + token;
        Map<String,Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userinfo", user.getUser());
        response.getWriter().write(objectMapper.writeValueAsString(ResultUtil.success(result)));

    }
}
