package com.board.api.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RestDocsConfig {
    private final SecuritySchemeConfig securitySchemeConfig;

    @Autowired
    public RestDocsConfig(SecuritySchemeConfig securitySchemeConfig) {
        this.securitySchemeConfig = securitySchemeConfig;
    }

    @Bean
    public OpenAPI restDocsOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .components(new Components().addSecuritySchemes(SecuritySchemeConfig.BEARER_AUTH, securitySchemeConfig.createSecurityScheme()))
                .security(Collections.singletonList(securitySchemeConfig.createSecurityRequirement()));
    }

    private Info createApiInfo() {
        return new Info()
                .title("게시판 이용자의 포인트 관리 서비스 (토이 프로젝트) - Rest Docs")
                .description("Java 17 & Spring Boot 3.3.1")
                .version("1.0.0");
    }

}
