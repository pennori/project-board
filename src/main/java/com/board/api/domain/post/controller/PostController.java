package com.board.api.domain.post.controller;

import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.dto.request.PostModifyRequest;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.service.*;
import com.board.api.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostCreateService postCreateService;
    private final PostModifyService postModifyService;
    private final PostViewService postViewService;
    private final PostListViewService postListViewService;
    private final PostDeleteService postDeleteService;


    @PostMapping("/post")
    public ResponseEntity<ApiResponse<PostCreationDto>> createPost(@Valid @RequestBody PostCreateRequest request) {
        PostCreationDto dto = postCreateService.createPost(request);

        return ResponseEntity.ok().body(
                ApiResponse.<PostCreationDto>builder()
                        .status(HttpStatus.OK)
                        .data(dto)
                        .build()
        );
    }

    @GetMapping("/post")
    public ResponseEntity<ApiResponse<Page<PostListViewDto>>> listViewPost(@PageableDefault Pageable pageable) {
        Page<PostListViewDto> bunchOfDto = postListViewService.listViewPost(pageable);

        return ResponseEntity.ok().body(
                ApiResponse.<Page<PostListViewDto>>builder()
                        .status(HttpStatus.OK)
                        .data(bunchOfDto)
                        .build()
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<PostViewDto>> viewPost(@PathVariable String postId) {
        if (!postId.chars().allMatch(Character::isDigit)) {
            throw new PostException("id 값이 숫자 형식이 아닙니다.");
        }

        PostViewDto dto = postViewService.viewPost(Long.parseLong(postId));

        return ResponseEntity.ok().body(
                ApiResponse.<PostViewDto>builder()
                        .status(HttpStatus.OK)
                        .data(dto)
                        .build()
        );
    }

    @PutMapping("/post")
    public ResponseEntity<ApiResponse<PostModifyDto>> modifyPost(@Valid @RequestBody PostModifyRequest postRequest) {
        PostModifyDto dto = postModifyService.modifyPost(postRequest);

        return ResponseEntity.ok().body(
                ApiResponse.<PostModifyDto>builder()
                        .status(HttpStatus.OK)
                        .data(dto)
                        .build()
        );
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<?>> deletePost(@PathVariable Long postId) {
        postDeleteService.deletePost(postId);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
