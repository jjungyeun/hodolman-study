package com.wonjung.hodolstudy1.config

import com.wonjung.hodolstudy1.repository.MemberSessionRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    val authResolver: AuthResolver
): WebMvcConfigurer {

//     // 서버에서 CORS 해결하는 코드
//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("http://localhost:5173")
//    }

    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry.addInterceptor(AuthInterceptor())
//            .excludePathPatterns("/css/**", "/*.ico", "/error")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authResolver)
    }
}