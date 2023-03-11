package info.phj233.onlinechat.handler;

import info.phj233.onlinechat.util.ResultEnum;
import info.phj233.onlinechat.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 暂无权限处理类
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.handler
 * @className: AccessDeniedHandlerImpl
 * @author: phj233
 * @date: 2023/3/10 22:41
 * @version: 1.0
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ResultUtil.error(ResultEnum.FORBIDDEN, "暂无权限");
    }
}
