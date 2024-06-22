package com.board.api.domain.member.dto.response;

import com.board.api.global.dto.response.CommonResponse;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class SignUpResponse extends CommonResponse {
    public SignUpResponse(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }
}
