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
        ApiMetadata apiMetadata = new ApiMetadata();
        SecurityConfig securityConfig = new SecurityConfig();

        return new OpenAPI()
                .info(apiMetadata.createApiInfo())
                .components(new Components().addSecuritySchemes(SecurityConfig.BEARER_AUTH, securityConfig.createSecurityScheme()))
                .security(Collections.singletonList(securityConfig.createSecurityRequirement()));
    }

    private static class ApiMetadata {
        public Info createApiInfo() {
            return new Info()
                    .title("게시판 이용자의 포인트 관리 서비스 (토이 프로젝트)")
                    .description("Java 17 & Spring Boot 3.3.1")
                    .version("1.0.0");
        }
    }

    private static class SecurityConfig {
        public static final String BEARER_AUTH = "bearerAuth";
        public static final String AUTH_HEADER_NAME = "Authorization";
        public static final String BEARER_FORMAT = "JWT";

        public SecurityRequirement createSecurityRequirement() {
            return new SecurityRequirement().addList(BEARER_AUTH);
        }

        public SecurityScheme createSecurityScheme() {
            return new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat(BEARER_FORMAT)
                    .in(SecurityScheme.In.HEADER)
                    .name(AUTH_HEADER_NAME);
        }
    }
}