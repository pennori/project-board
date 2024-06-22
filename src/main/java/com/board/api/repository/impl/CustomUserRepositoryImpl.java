package com.board.api.repository.impl;

import com.board.api.entity.AppUser;
import com.board.api.entity.QAppUser;
import com.board.api.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    public CustomUserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public AppUser getUserByUserId(String userId) {
        QAppUser qau = QAppUser.appUser;
        JPAQuery<AppUser> query = queryFactory.selectFrom(qau).where(qau.userId.eq(userId));

        return query.fetchOne();
    }
}
