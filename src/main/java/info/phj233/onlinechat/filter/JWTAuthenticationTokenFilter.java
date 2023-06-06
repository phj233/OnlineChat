package info.phj233.onlinechat.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.service.UserService;
import info.phj233.onlinechat.service.impl.UserDetailsServiceImpl;
import info.phj233.onlinechat.util.result.E;
import info.phj233.onlinechat.util.result.R;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.filter
 * @className: JWTAuthenticationTokenFilter
 * @author: phj233
 * @date: 2023/3/11 12:03
 * @version: 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        // 获取请求头中的token
        String jwt = request.getHeader(JWTConfig.tokenHeader);
        // 如果请求头中有token且开头为指定字符串
        if (jwt != null && !jwt.equals("") && jwt.startsWith(JWTConfig.tokenPrefix)){
            // 截取JWT前缀
            String token = jwt.replace(JWTConfig.tokenPrefix, "");
            // 解析JWT 获取用户名、密码
            String username = JWT.decode(token).getClaim("username").asString();
            String password = JWT.decode(token).getClaim("password").asString();
            //如果用户名不为空且SecurityContext中没有认证信息，则进行认证
            if (!ObjectUtils.isEmpty(username) && ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication())) {
                // 从UserDetailService获取用户信息
                UserDetailImpl userDetails = (UserDetailImpl) userDetailsService.loadUserByUsername(username);
                // 获取用户权限
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                if (!ObjectUtils.isEmpty(authorities)) {
                    //判断token是否有效
                    if (!userService.checkToken(token)) {
                        log.info("token已过期");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(
                                objectMapper.writeValueAsString(
                                        R.error(E. TOKEN_EXPIRED)
                                ));
                        return;
                    }
                    // 将authentication放入SecurityContext中;
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(userDetails, password, authorities));
                }
            }
        }
        chain.doFilter(request,response);
    }
}
