package com.board.api.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpDto {
    private final String memberId;

    @Builder
    public SignUpDto(Long memberId) {
        this.memberId = String.valueOf(memberId);
    }
}
