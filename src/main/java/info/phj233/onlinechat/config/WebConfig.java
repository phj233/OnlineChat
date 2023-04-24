package info.phj233.onlinechat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @projectName: OnlineChat
 * @package: info.phj233.onlinechat.config
 * @className: WebConfig
 * @author: phj233
 * @date: 2023/4/21 15:35
 * @version: 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String s = System.getProperty("user.dir") + "/upload/";
        //放行项目下的upload
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + s);
    }
}
