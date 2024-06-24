package com.board.api.domain.member.repository;

public interface QMemberRepository {
    Long getPointByEmail(String email);
}
