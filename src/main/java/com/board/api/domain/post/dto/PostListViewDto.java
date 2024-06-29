package com.board.api.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListViewDto {
    private String postId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Builder
    public PostListViewDto(Long postId, String title, String content, LocalDateTime updatedAt, Long updatedBy) {
        this.postId = String.valueOf(postId);
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.updatedBy = String.valueOf(updatedBy);
    }
}
