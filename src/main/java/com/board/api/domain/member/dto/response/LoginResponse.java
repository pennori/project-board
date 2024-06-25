package com.board.api.domain.member.dto.response;

import com.board.api.global.dto.response.AbstractResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class LoginResponse extends AbstractResponse {
    private final String token;

    @Builder
    public LoginResponse(int resultCode, String resultMsg, String token) {
        super(resultCode, resultMsg);
        this.token = token;
    }
}
