package com.zhou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description:
 * @Version:
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("标准接口")
                .apiInfo(apiInfo("Spring+Vue后台管理系统", "1.0"))
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zhou.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    public ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .description("Springboot+Vue后台管理系统")
                .termsOfServiceUrl("https://yuque.com/zfovikqdev928")
                .contact(new Contact("周益全", "https://yuque.com/zfovikqdev928", "1507998218@qq.com"))
                .version(version)
                .build();
    }
}
