package com.board.api.repository.impl;

import com.board.api.entity.Member;
import com.board.api.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    public CustomUserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Member getUserByEmail(String email) {
        return null;
    }
}
