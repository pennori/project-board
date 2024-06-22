package com.board.api.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class SignUpResponse extends CommonResponse{
    public SignUpResponse(int resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }
}
