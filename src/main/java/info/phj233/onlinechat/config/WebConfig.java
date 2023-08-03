package info.phj233.onlinechat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于配置静态资源的放行
 * @author phj233
 * @since 2023/4/21 15:35
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String s = System.getProperty("user.dir") + "/upload/";
        //放行项目下的upload
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + s);
    }

    /**
     * 全局跨域配置
     * @param registry 跨域注册器
     */
     @Override
     public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/**")
                 .allowedOrigins("*")
                 .allowedMethods("*")
                 .allowedHeaders("*")
                 .allowCredentials(true);
     }

}
