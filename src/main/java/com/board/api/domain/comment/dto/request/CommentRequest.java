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
    @NotBlank
    @Pattern(regexp = "^[0-9]*$")
    private String postId;
    @NotBlank
    @Size(min = 1, max = 255)
    private String content;
}
