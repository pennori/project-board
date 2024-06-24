package com.board.api.domain.post.repository.impl;

import com.board.api.domain.post.repository.QPostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository {
    private final JPAQueryFactory queryFactory;
}
