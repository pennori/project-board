package com.board.api.domain.member.dto.response;

import com.board.api.global.dto.response.CommonResponse;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class LoginResponse extends CommonResponse {
    private final String token;

    public LoginResponse(String resultCode, String resultMsg, String token) {
        super(resultCode, resultMsg);
        this.token = token;
    }
}
