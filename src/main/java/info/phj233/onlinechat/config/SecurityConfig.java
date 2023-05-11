package info.phj233.onlinechat.config;

import info.phj233.onlinechat.evaluator.SelfPermissionEvaluatorImpl;
import info.phj233.onlinechat.filter.JWTAuthenticationTokenFilter;
import info.phj233.onlinechat.handler.*;
import info.phj233.onlinechat.provider.AuthenticationProviderImpl;
import info.phj233.onlinechat.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security配置类
 * @author phj233
 * @since  2023/3/10 14:06
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 开启方法级别的权限认证
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
        http
                // 关闭csrf
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                // 配置无权限自定义处理类
                .accessDeniedHandler(accessDeniedHandler)
                // 配置未登录自定义处理类
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeHttpRequests(auth -> auth
                        //放行knife4j
                        .requestMatchers(
                                "/doc.html",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/upload/**",
                                "/login",
                                "/user/register",
                                "/user/info",
                                "/user/checktoken",
                        "/user/avatar").permitAll()
                        .anyRequest().authenticated()
                )
                // security提交form表单请求的接口地址 默认是/login
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 允许添加额外的用户详细信息服务以供使用
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
                .logoutSuccessHandler(logoutSuccessHandler);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
