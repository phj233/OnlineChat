package info.phj233.onlinechat.provider;

import info.phj233.onlinechat.model.UserDetailImpl;
import info.phj233.onlinechat.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

/**
 * 自定义登录验证类
 * @author phj233
 * @since  2023/3/12 21:27
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        // 通过用户名查询用户信息
        UserDetailImpl userDetails = (UserDetailImpl) userDetailsService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(userDetails)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 验证密码
        if (!new BCryptPasswordEncoder().matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        // 获取用户权限
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        // 将用户信息和权限封装到Authentication中
        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
