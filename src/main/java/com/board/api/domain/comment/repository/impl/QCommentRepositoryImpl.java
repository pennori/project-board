package com.board.api.domain.comment.repository.impl;

import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.entity.QComment;
import com.board.api.domain.comment.repository.QCommentRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QCommentRepositoryImpl implements QCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> getBunchOfCommentByPost(Long postId) {
        QComment comment = QComment.comment;

        JPAQuery<Comment> query = queryFactory.selectFrom(comment).where(comment.post.postId.eq(postId));

        return query.fetch();
    }
}
