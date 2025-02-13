package com.board.api.domain.comment.controller;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.service.CommentCreateService;
import com.board.api.domain.comment.service.CommentDeleteService;
import com.board.api.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private static final HttpStatus SUCCESS_STATUS = HttpStatus.OK;

    private final CommentCreateService commentCreateService;
    private final CommentDeleteService commentDeleteService;

    @PostMapping("/comment")
    public ResponseEntity<ApiResponse<CommentDto>> createComment(@Valid @RequestBody CommentRequest request) {
        CommentDto createdComment = commentCreateService.createComment(request);
        return buildApiResponse(createdComment);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable @Positive Long commentId) {
        commentDeleteService.deleteComment(commentId);
        return buildEmptyApiResponse();
    }

    private <T> ResponseEntity<ApiResponse<T>> buildApiResponse(T data) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .status(SUCCESS_STATUS)
                        .data(data)
                        .build()
        );
    }

    private ResponseEntity<ApiResponse<?>> buildEmptyApiResponse() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(SUCCESS_STATUS)
                        .build()
        );
    }
}