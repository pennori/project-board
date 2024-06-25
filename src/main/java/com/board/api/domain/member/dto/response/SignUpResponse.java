package com.board.api.domain.member.dto.response;

import com.board.api.global.dto.response.AbstractResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class SignUpResponse extends AbstractResponse {
    private final String memberId;

    @Builder
    public SignUpResponse(String resultCode, String resultMsg, String memberId) {
        super(resultCode, resultMsg);
        this.memberId = memberId;
    }
}
