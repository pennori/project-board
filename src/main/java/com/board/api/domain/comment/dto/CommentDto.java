package com.board.api.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDto {
    private final String postId;
    private final String commentId;

    @Builder
    public CommentDto(Long postId, Long commentId) {
        this.postId = String.valueOf(postId);
        this.commentId = String.valueOf(commentId);
    }
}
