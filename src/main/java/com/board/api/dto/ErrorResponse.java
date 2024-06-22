package com.board.api.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ErrorResponse extends CommonResponse {
    public ErrorResponse(int resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }
}
