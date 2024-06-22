package com.board.api.domain.member.repository.impl;

import com.board.api.domain.member.repository.CustomMemberRepository;
import com.board.api.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

    public CustomMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Member getMemberByEmail(String email) {
        return null;
    }
}
