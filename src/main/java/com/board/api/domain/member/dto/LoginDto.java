package com.board.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginDto {
    private final String token;

    @Builder
    public LoginDto(String token) {
        this.token = token;
    }
}
