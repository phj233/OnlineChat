package info.phj233.onlinechat.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import info.phj233.onlinechat.config.JWTConfig;
import info.phj233.onlinechat.model.dto.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.filter
 * @className: JWTAuthenticationTokenFilter
 * @author: phj233
 * @date: 2023/3/11 12:03
 * @version: 1.0
 */
public class JWTAuthenticationTokenFilter extends BasicAuthenticationFilter {
    public JWTAuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取请求头中的token
        String tokenHeader = request.getHeader(JWTConfig.tokenHeader);
        if (tokenHeader != null && tokenHeader.startsWith(JWTConfig.tokenPrefix)) {
            try {
                // 截取JWT前缀
                String token = tokenHeader.replace(JWTConfig.tokenPrefix, "");
                // 解析JWT 获取用户名、密码
                String username = JWT.decode(token).getClaim("username").asString();
                String uid = JWT.decode(token).getClaim("uid").asString();
                if (!ObjectUtils.isEmpty(username) && !ObjectUtils.isEmpty(uid)) {
                    // 获取用户权限
                    String authorities = JWT.decode(token).getClaim("authorities").asString();
                    if (!ObjectUtils.isEmpty(authorities)) {
                        // 将权限封装到List中
                        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                        String[] authority = authorities.split(",");
                        for (String s : authority) {
                            grantedAuthorities.add(new SimpleGrantedAuthority(s));
                        }
                        // 将用户信息和权限封装到Authentication中
                        User user = new User();
                        user.setUsername(username);
                        user.setUid(Integer.valueOf(uid));
                        user.setRole(grantedAuthorities.toString());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, uid, grantedAuthorities);
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
