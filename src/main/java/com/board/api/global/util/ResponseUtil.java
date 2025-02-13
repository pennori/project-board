package com.board.api.global.util;

import com.board.api.global.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.OK;

    // Main method to create ApiResponse
    private static <T> ApiResponse<T> createResponse(T data) {
        return ApiResponse.<T>builder()
                .status(DEFAULT_STATUS)
                .data(data)
                .build();
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildResponseWithData(T data) {
        return ResponseEntity.ok(createResponse(data));
    }

    public static ResponseEntity<ApiResponse<?>> buildEmptyResponse() {
        return ResponseEntity.ok(createResponse(null));
    }
}