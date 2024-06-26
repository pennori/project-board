package com.board.api.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUp {
    private final String memberId;

    @Builder
    public SignUp(Long memberId) {
        this.memberId = String.valueOf(memberId);
    }
}
