package com.board.api.domain.post.dto;

import com.board.api.domain.comment.dto.CommentViewDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostViewDto {
    private String postId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private List<CommentViewDto> bunchOfCommentViewDto;

    @Builder
    public PostViewDto(Long postId, String title, String content, LocalDateTime updatedAt, Long updatedBy, List<CommentViewDto> bunchOfCommentViewDto) {
        this.postId = String.valueOf(postId);
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.updatedBy = String.valueOf(updatedBy);
        this.bunchOfCommentViewDto = bunchOfCommentViewDto;
    }
}
