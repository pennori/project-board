package com.board.api.domain.member.repository.impl;


import com.board.api.domain.member.entity.QMemberPoint;
import com.board.api.domain.member.repository.QMemberPointRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QMemberPointRepositoryImpl implements QMemberPointRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long getPoint(Long memberId) {
        QMemberPoint qmp = QMemberPoint.memberPoint;
        JPAQuery<Long> query =
                queryFactory
                        .select(qmp.score)
                        .from(qmp)
                        .where(qmp.member.memberId.eq(memberId));

        return query.fetchOne();
    }
}
