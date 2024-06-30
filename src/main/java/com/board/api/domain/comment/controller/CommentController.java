package com.board.api.domain.comment.controller;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.service.CommentCreateService;
import com.board.api.domain.comment.service.CommentDeleteService;
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
public class CommentController {
    private final CommentCreateService commentCreateService;
    private final CommentDeleteService commentDeleteService;

    @PostMapping("/comment")
    public ResponseEntity<ApiResponse<CommentDto>> createComment(@Valid @RequestBody CommentRequest request){
        CommentDto dto = commentCreateService.createComment(request);

        return ResponseEntity.ok().body(
                ApiResponse.<CommentDto>builder()
                        .status(HttpStatus.OK)
                        .data(dto)
                        .build()
        );
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable Long commentId) {
        commentDeleteService.deleteComment(commentId);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
