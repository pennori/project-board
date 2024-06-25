package com.board.api.global.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ErrorResponse extends AbstractResponse {
    @Builder
    public ErrorResponse(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }
}
