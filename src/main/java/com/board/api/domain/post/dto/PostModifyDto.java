package com.board.api.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostModifyDto {
    private final String postId;

    @Builder
    public PostModifyDto(Long postId) {
        this.postId = String.valueOf(postId);
    }
}
