package com.board.api.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreationDto {
    private final String postId;

    @Builder
    public PostCreationDto(Long postId) {
        this.postId = String.valueOf(postId);
    }
}
