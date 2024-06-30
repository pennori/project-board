package com.board.api.global.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final String resultCode;
    private final String resultMessage;
    private final T data;

    @Builder
    public ApiResponse(HttpStatus status, T data) {
        this.resultCode = String.valueOf(status.value());
        this.resultMessage = status.name();
        this.data = data;
    }
}
