package top.phj233.backend_sb3.config

import cn.dev33.satoken.`fun`.SaFunction
import cn.dev33.satoken.interceptor.SaInterceptor
import cn.dev33.satoken.jwt.StpLogicJwtForStateless
import cn.dev33.satoken.router.SaRouter
import cn.dev33.satoken.stp.StpLogic
import cn.dev33.satoken.stp.StpUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class SaTokenConfigure : WebMvcConfigurer {
    // 注册拦截器
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(SaInterceptor{
            SaRouter.match("/**").notMatch("/user/login", "/user/register","/openapi.html")
                .check(SaFunction {
                    StpUtil.checkLogin()
                })
        }).addPathPatterns("/**")
    }

    @Bean
    fun getStpLogicJwt(): StpLogic {
        return StpLogicJwtForStateless()
    }
}


