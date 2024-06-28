package com.board.api.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "postId 은/는 필수 값입니다.")
    @Pattern(regexp = "^[0-9]*$", message = "postId 은/는 숫자만 가능합니다.")
    private String postId;
    @NotBlank(message = "content 은/는 필수 값입니다.")
    @Size(min = 1, max = 255, message = "content 은/는 1 ~ 255자 입니다.")
    private String content;
}
