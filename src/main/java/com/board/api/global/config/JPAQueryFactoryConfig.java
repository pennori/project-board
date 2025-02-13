package com.board.api.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JPAQueryFactoryConfig {
    private final EntityManager em;

    public static final String BEAN_NAME = "jpaQueryFactory";

    @Bean(name = BEAN_NAME)
    public JPAQueryFactory createQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
