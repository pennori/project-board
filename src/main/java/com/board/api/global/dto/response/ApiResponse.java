package com.board.api.global.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final String resultCode;
    private final String resultMessage;
    private final T data;

    @Builder
    public ApiResponse(int resultCode, String resultMessage, T data) {
        this.resultCode = String.valueOf(resultCode);
        this.resultMessage = resultMessage;
        this.data = data;
    }
}
