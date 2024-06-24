package com.board.api.domain.point.repository.impl;

import com.board.api.domain.point.entity.QPoint;
import com.board.api.domain.point.repository.CustomPointRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPointRepositoryImpl implements CustomPointRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long getTotalPointByMemberId(Long memberId) {
        QPoint p = QPoint.point;
        JPAQuery<Long> query = queryFactory.select(p.total).from(p).where(p.member.memberId.eq(memberId));

        return query.fetchOne();
    }
}
