package info.phj233.onlinechat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.config
 * @className: SecurityConfig
 * @author: phj233
 * @date: 2023/3/10 14:06
 * @version: 1.0
 */
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
