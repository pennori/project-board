package com.board.api.domain.member.repository.impl;

import com.board.api.domain.member.entity.QMember;
import com.board.api.domain.member.repository.QMemberRepository;
import com.board.api.domain.point.entity.QPoint;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QMemberRepositoryImpl implements QMemberRepository {

    private final JPAQueryFactory queryFactory;
    
    @Override
    public Long getScoreByEmail(String email) {
        QPoint p = QPoint.point;
        QMember m = QMember.member;

        JPAQuery<Long> query = queryFactory.select(p.score).from(m).innerJoin(p).on(m.memberId.eq(p.member.memberId)).where(m.email.eq(email));

        return query.fetchOne();
    }
}
