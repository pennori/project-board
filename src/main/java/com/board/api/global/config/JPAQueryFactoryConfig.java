package com.board.api.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JPAQueryFactoryConfig {
    private final EntityManager em; // 변수 이름을 더 간결하게 변경

    public static final String BEAN_NAME = "jpaQueryFactory"; // 빈 이름을 상수로 관리

    @Bean(name = BEAN_NAME)
    public JPAQueryFactory createQueryFactory() {
        return new JPAQueryFactory(em); // 메서드 이름을 작용에 맞게 변경
    }
}
