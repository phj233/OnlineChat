package info.phj233.onlinechat.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import info.phj233.onlinechat.config.JWTConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

    final private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取请求头中的token
        String tokenHeader = request.getHeader(JWTConfig.tokenHeader);
        // 如果请求头中有token且开头为指定字符串
        if (tokenHeader != null && tokenHeader.startsWith(JWTConfig.tokenPrefix)) {
            try {
                // 截取JWT前缀
                String token = tokenHeader.replace(JWTConfig.tokenPrefix, "");
                // 解析JWT 获取用户名、密码
                String username = JWT.decode(token).getClaim("username").asString();
                //如果用户名不为空且SecurityContext中没有认证信息，则进行认证
                if (!ObjectUtils.isEmpty(username) && !ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication())) {
                    // 从UserDetailService获取用户信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    // 获取用户权限
                    String authorities = userDetails.getAuthorities().toString();
                    if (!ObjectUtils.isEmpty(authorities)) {
                        //判断token是否有效
                        if (JWT.decode(token).getExpiresAt().getTime() < System.currentTimeMillis()) {
                            log.info("token已过期");
                            throw new RuntimeException("token已过期");
                        }
                        // 将权限封装到List中
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (NumberFormatException | JWTVerificationException e) {
                throw new RuntimeException(e);
            }
        }
        chain.doFilter(request, response);
    }
}
