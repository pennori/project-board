package com.board.api.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostCreateRequest {

    private String postId;
    @NotBlank(message = "title 은/는 필수 값입니다.")
    @Size(min = 1, max = 255, message = "title 은/는 1 ~ 255자 입니다.")
    private String title;
    @NotBlank(message = "content 은/는 필수 값입니다.")
    @Size(min = 1, max = 255, message = "content size 은/는 1 ~ 255자 입니다.")
    private String content;
}
