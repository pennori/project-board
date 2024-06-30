package com.board.api.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .components(new Components().addSecuritySchemes("bearerAuth", getSecurityScheme()))
                .security(Collections.singletonList(getSecurityRequirement()));
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement().addList("bearerAuth");
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    private Info getApiInfo() {
        return new Info()
                .title("게시판 이용자의 포인트 관리 서비스 (토이 프로젝트)")
                .description("Java 17 & Spring Boot 3.3.1")
                .version("1.0.0");
    }
}
