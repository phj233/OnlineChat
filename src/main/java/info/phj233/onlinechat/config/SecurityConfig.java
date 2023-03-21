package info.phj233.onlinechat.config;

import info.phj233.onlinechat.evaluator.SelfPermissionEvaluatorImpl;
import info.phj233.onlinechat.filter.JWTAuthenticationTokenFilter;
import info.phj233.onlinechat.handler.*;
import info.phj233.onlinechat.provider.AuthenticationProviderImpl;
import info.phj233.onlinechat.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.config
 * @className: SecurityConfig
 * @author: phj233
 * @date: 2023/3/10 14:06
 * @version: 1.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    private final AuthenticationFailureHandlerImpl authenticationFailureHandler;
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AuthenticationProviderImpl authenticationProvider;
    private final JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final UserDetailsServiceImpl userDetailsService;
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new SelfPermissionEvaluatorImpl());
        return handler;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/user/register").permitAll()
                )
                // security提交form表单请求的接口地址 默认是/login/userLogin
                // 配置未登录自定义处理类
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .authenticationProvider(authenticationProvider)
                .formLogin()
                // 配置登录成功自定义处理类
                .successHandler(authenticationSuccessHandler)
                // 配置登录失败自定义处理类
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                // 配置退出成功自定义处理类
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                // 配置没有权限自定义处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .headers().cacheControl();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
