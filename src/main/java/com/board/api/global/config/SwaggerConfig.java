package com.board.api.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private final SecuritySchemeConfig securitySchemeConfig;

    @Autowired
    public SwaggerConfig(SecuritySchemeConfig securitySchemeConfig) {
        this.securitySchemeConfig = securitySchemeConfig;
    }


    @Bean
    public OpenAPI openAPI() {
        ApiMetadata apiMetadata = new ApiMetadata();

        return new OpenAPI()
                .info(apiMetadata.createApiInfo())
                .components(new Components().addSecuritySchemes(SecuritySchemeConfig.BEARER_AUTH, securitySchemeConfig.createSecurityScheme()))
                .security(Collections.singletonList(securitySchemeConfig.createSecurityRequirement()));
    }

    private static class ApiMetadata {
        public Info createApiInfo() {
            return new Info()
                    .title("게시판 이용자의 포인트 관리 서비스 (토이 프로젝트)")
                    .description("Java 17 & Spring Boot 3.3.1")
                    .version("1.0.0");
        }
    }

}