package com.board.api.domain.post.controller;

import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostRequest;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.service.PostService;
import com.board.api.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<PostCreationDto>> createPost(@Valid @RequestBody PostRequest request) {
        PostCreationDto dto = postService.createPost(request);

        return ResponseEntity.ok().body(
                ApiResponse.<PostCreationDto>builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .data(dto)
                        .build()
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<PostViewDto>> viewPost(@PathVariable String postId) {
        if (!postId.chars().allMatch(Character::isDigit)) {
            throw new PostException("id 값이 숫자 형식이 아닙니다.");
        }

        PostViewDto postViewDto = postService.viewPost(Long.parseLong(postId));

        return ResponseEntity.ok().body(
                ApiResponse.<PostViewDto>builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .data(postViewDto)
                        .build()
        );
    }
}
