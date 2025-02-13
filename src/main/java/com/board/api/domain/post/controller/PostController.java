package com.board.api.domain.post.controller;

import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.dto.request.PostModifyRequest;
import com.board.api.domain.post.service.*;
import com.board.api.global.dto.response.ApiResponse;
import com.board.api.global.util.ResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
        return ResponseUtil.buildResponseWithData(dto);
    }

    @GetMapping("/post")
    public ResponseEntity<ApiResponse<Page<PostListViewDto>>> listViewPost(@PageableDefault Pageable pageable) {
        Page<PostListViewDto> bunchOfDto = postListViewService.listViewPost(pageable);
        return ResponseUtil.buildResponseWithData(bunchOfDto);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<PostViewDto>> viewPost(@PathVariable @Positive Long postId) {
        PostViewDto dto = postViewService.viewPost(postId);
        return ResponseUtil.buildResponseWithData(dto);
    }

    @PutMapping("/post")
    public ResponseEntity<ApiResponse<PostModifyDto>> modifyPost(@Valid @RequestBody PostModifyRequest postRequest) {
        PostModifyDto dto = postModifyService.modifyPost(postRequest);
        return ResponseUtil.buildResponseWithData(dto);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable @Positive Long postId) {
        postDeleteService.deletePost(postId);
        return ResponseUtil.buildResponseWithData(null);
    }

}