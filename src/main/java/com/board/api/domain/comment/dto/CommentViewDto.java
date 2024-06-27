package com.board.api.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentViewDto {
    private String commentId;
    private String content;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Builder
    public CommentViewDto(Long commentId, String content, LocalDateTime updatedAt, Long updatedBy) {
        this.commentId = String.valueOf(commentId);
        this.content = content;
        this.updatedAt = updatedAt;
        this.updatedBy = String.valueOf(updatedBy);
    }
}
