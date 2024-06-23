package com.board.api.global.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ErrorResponse extends CommonResponse {
    public ErrorResponse(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }
}
