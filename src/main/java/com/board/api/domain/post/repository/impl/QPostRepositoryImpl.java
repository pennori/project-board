package com.board.api.domain.post.repository.impl;

import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.entity.QPost;
import com.board.api.domain.post.repository.QPostRepository;
import com.board.api.global.util.QueryDSLUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository {
    private final JPAQueryFactory queryFactory;
    public final QueryDSLUtil queryDSLUtil;

    @Override
    public Page<PostListViewDto> getList(Pageable pageable) {
        QPost qPost = QPost.post;
        List<PostListViewDto> fetch = queryFactory.select(
                        Projections.constructor(PostListViewDto.class,
                                qPost.postId,
                                qPost.title,
                                qPost.content,
                                qPost.updatedAt,
                                qPost.updatedBy
                        )
                ).from(qPost)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(queryDSLUtil.getOrderSpecifier(pageable.getSort(), qPost).toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Long> count = queryFactory.select(qPost.count()).from(qPost);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

}
