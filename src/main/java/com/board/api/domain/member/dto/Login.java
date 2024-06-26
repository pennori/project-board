package com.board.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Login {
    private final String token;

    @Builder
    public Login(String token) {
        this.token = token;
    }
}
