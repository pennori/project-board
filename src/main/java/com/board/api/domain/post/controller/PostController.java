package com.board.api.domain.post.controller;

import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.dto.request.PostModifyRequest;
import com.board.api.domain.post.service.*;
import com.board.api.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
        return buildApiResponse(dto);
    }

    @GetMapping("/post")
    public ResponseEntity<ApiResponse<Page<PostListViewDto>>> listViewPost(@PageableDefault Pageable pageable) {
        Page<PostListViewDto> bunchOfDto = postListViewService.listViewPost(pageable);
        return buildApiResponse(bunchOfDto);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<PostViewDto>> viewPost(@PathVariable @Positive Long postId) {
        PostViewDto dto = postViewService.viewPost(postId);
        return buildApiResponse(dto);
    }

    @PutMapping("/post")
    public ResponseEntity<ApiResponse<PostModifyDto>> modifyPost(@Valid @RequestBody PostModifyRequest postRequest) {
        PostModifyDto dto = postModifyService.modifyPost(postRequest);
        return buildApiResponse(dto);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable @Positive Long postId) {
        postDeleteService.deletePost(postId);
        return buildApiResponse(null);
    }

    // Extracted helper function for building consistent ApiResponse
    private <T> ResponseEntity<ApiResponse<T>> buildApiResponse(T data) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .data(data)
                .build());
    }
}