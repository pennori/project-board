package com.board.api.domain.comment.repository;

import com.board.api.domain.comment.entity.Comment;

import java.util.List;

public interface QCommentRepository {
    List<Comment> getBunchOfCommentByPost(Long postId);
}
