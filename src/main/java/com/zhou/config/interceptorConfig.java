package com.zhou.config;

import com.zhou.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description:
 * @Version:
 */
@Configuration
public class interceptorConfig implements WebMvcConfigurer {


    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                // 拦截规则
                .addPathPatterns("/**")
                // 放行规则
                .excludePathPatterns("/user/login", "/user/register", "/**/export", "/**/import");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
