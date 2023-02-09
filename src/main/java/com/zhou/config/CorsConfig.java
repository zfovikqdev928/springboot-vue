package com.zhou.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description: 解决 Cors 跨域问题
 * @Version:
 */
@Configuration
public class CorsConfig {
    // 当前跨域请求最大有效时长，默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 访问源的地址
        corsConfiguration.addAllowedHeader("*"); // 访问源的请求头
        corsConfiguration.addAllowedMethod("*"); // 访问源的请求方法
        corsConfiguration.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", corsConfiguration); // 对接口配置跨域设置
        return new CorsFilter(source);
    }

}
