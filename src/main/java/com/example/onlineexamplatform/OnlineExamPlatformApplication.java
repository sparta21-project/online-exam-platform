package com.example.onlineexamplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnlineExamPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineExamPlatformApplication.class, args);
    }

}
