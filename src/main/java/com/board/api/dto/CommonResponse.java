package com.board.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public abstract class CommonResponse {
    private String resultCode;
    private String resultMsg;
}
