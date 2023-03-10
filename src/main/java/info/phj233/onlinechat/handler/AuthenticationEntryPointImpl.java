package info.phj233.onlinechat.handler;

import info.phj233.onlinechat.util.ResultEnum;
import info.phj233.onlinechat.util.ResultUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用户未登录处理类
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.handler
 * @className: AuthenticationEntryPointImpl
 * @author: phj233
 * @date: 2023/3/10 22:44
 * @version: 1.0
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResultUtil.error(ResultEnum.NOT_LOGIN, "用户未登录");
    }
}
