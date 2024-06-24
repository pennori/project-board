package com.board.api.domain.point.repository.impl;

import com.board.api.domain.point.entity.QPoint;
import com.board.api.domain.point.repository.QPointRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QPointRepositoryImpl implements QPointRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long getScoreByMemberId(Long memberId) {
        QPoint p = QPoint.point;
        JPAQuery<Long> query = queryFactory.select(p.score).from(p).where(p.member.memberId.eq(memberId));

        return query.fetchOne();
    }
}
