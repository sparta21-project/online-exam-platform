package com.example.onlineexamplatform.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()) // 공통적으로 사용되는 구성요소 등록 용도 , SecuritySchemes, Schemas, Responses, RequestBodies 등
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Online Exam Platform API") // API 제목
                .description("온라인 시험 플랫폼 백엔드 API 문서") // API 설명
                .version("1.0.0"); // API 버전
    }
}
