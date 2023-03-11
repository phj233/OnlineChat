package info.phj233.onlinechat.handler;

import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.dto.User;
import info.phj233.onlinechat.util.JWTUtil;
import info.phj233.onlinechat.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String token = JWTUtil.generateToken(user);
        token = JWTConfig.tokenPrefix + token;
        Map<String,Object> result = new HashMap<>();
        result.put("token", token);
        ResultUtil.success(result);

    }
}
