package com.board.api.domain.comment.controller;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.service.CommentService;
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
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<ApiResponse<CommentDto>> createComment(@Valid @RequestBody CommentRequest request){
        CommentDto dto = commentService.createComment(request);

        return ResponseEntity.ok().body(
                ApiResponse.<CommentDto>builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .data(dto)
                        .build()
        );
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .build()
        );
    }
}
