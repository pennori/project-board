package com.board.api.global.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractResponse {
    private final String resultCode;
    private final String resultMsg;

    public AbstractResponse(int resultCode, String resultMsg) {
        this.resultCode = String.valueOf(resultCode);
        this.resultMsg = resultMsg;
    }
}
