package com.zhou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@MapperScan("com.zhou")
@RestController
public class VueBlogApplication {


    public static void main(String[] args) {
        SpringApplication.run(VueBlogApplication.class, args);
    }


}
