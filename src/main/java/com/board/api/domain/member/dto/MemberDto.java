package com.board.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    private final Long memberId;
    private final String email;
    private final String password;
    private final String role;
}
