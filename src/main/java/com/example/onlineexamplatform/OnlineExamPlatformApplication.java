package com.example.onlineexamplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class OnlineExamPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineExamPlatformApplication.class, args);
	}

}
