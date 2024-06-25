package com.board.api.domain.member.repository.impl;


import com.board.api.domain.member.repository.QMemberPointRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QMemberPointRepositoryImpl implements QMemberPointRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long getScoreByMemberId(Long memberId) {
//        QPoint p = QPoint.point;
//        JPAQuery<Long> query = queryFactory.select(p.score).from(p).where(p.member.memberId.eq(memberId));
//
//        return query.fetchOne();
        return null;
    }
}
