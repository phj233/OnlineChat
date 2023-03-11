package info.phj233.onlinechat.handler;

import info.phj233.onlinechat.util.ResultEnum;
import info.phj233.onlinechat.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 登录失败处理类
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.handler
 * @className: AuthenticationFailureHandlerImpl
 * @author: phj233
 * @date: 2023/3/10 22:46
 * @version: 1.0
 */
@Component
@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)  {
        switch (exception.getClass().getName()) {
            case "UsernameNotFoundException" -> {
                log.info("【登录失败】" + exception.getMessage());
                ResultUtil.error(ResultEnum.SYSTEM_ERROR, "用户不存在");
            }
            case "BadCredentialsException" -> {
                log.info("【登录失败】" + exception.getMessage());
                ResultUtil.error(ResultEnum.SYSTEM_ERROR, "密码错误");
            }
            case "LockedException" -> {
                log.info("【登录失败】" + exception.getMessage());
                ResultUtil.error(ResultEnum.SYSTEM_ERROR, "账户被锁定");
            }
            default -> {
                log.info("【登录失败】" + exception.getMessage());
                ResultUtil.error(ResultEnum.SYSTEM_ERROR, "登录失败");
            }
        }
    }
}
