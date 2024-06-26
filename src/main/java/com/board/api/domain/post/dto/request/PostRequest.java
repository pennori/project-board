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
public class PostRequest {
    private String postId;
    @NotBlank(message = "title is a required input.")
    @Size(min = 1, max = 255, message = "title size is between 1 and 255")
    private String title;
    @NotBlank(message = "content is a required input.")
    @Size(min = 1, max = 255, message = "content size is between 1 and 255")
    private String content;
}
