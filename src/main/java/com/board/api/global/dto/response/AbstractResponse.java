package com.board.api.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public abstract class AbstractResponse {
    private String resultCode;
    private String resultMsg;
}
