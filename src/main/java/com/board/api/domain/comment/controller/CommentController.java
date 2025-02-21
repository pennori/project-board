package com.board.api.domain.comment.controller;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.service.CommentCreateService;
import com.board.api.domain.comment.service.CommentDeleteService;
import com.board.api.global.dto.response.ApiResponse;
import com.board.api.global.util.ResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentCreateService commentCreateService;
    private final CommentDeleteService commentDeleteService;

    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<CommentDto>> createComment(@Valid @RequestBody CommentRequest request) {
        CommentDto createdComment = commentCreateService.createComment(request);
        return ResponseUtil.buildResponseWithData(createdComment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable @Positive Long commentId) {
        commentDeleteService.deleteComment(commentId);
        return ResponseUtil.buildEmptyResponse();
    }

}