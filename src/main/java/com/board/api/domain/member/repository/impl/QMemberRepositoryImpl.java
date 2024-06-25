package com.board.api.domain.member.repository.impl;

import com.board.api.domain.member.entity.QMember;
import com.board.api.domain.member.entity.QMemberPoint;
import com.board.api.domain.member.repository.QMemberRepository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QMemberRepositoryImpl implements QMemberRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long getScoreByEmail(String email) {
        QMemberPoint qmp = QMemberPoint.memberPoint;
        QMember qm = QMember.member;

        JPAQuery<Long> query = queryFactory.select(qmp.score).from(qm).innerJoin(qmp).on(qm.memberId.eq(qmp.member.memberId)).where(qm.email.eq(email));

        return query.fetchOne();

    }
}
