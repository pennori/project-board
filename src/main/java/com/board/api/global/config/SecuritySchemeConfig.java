package com.board.api.global.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuritySchemeConfig {
    public static final String BEARER_AUTH = "bearerAuth";
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String BEARER_FORMAT = "JWT";

    public SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList(BEARER_AUTH);
    }

    @Bean
    public SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat(BEARER_FORMAT)
                .in(SecurityScheme.In.HEADER)
                .name(AUTH_HEADER_NAME);
    }
}
