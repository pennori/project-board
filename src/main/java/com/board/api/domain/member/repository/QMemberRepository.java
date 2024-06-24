package com.board.api.domain.member.repository;

public interface QMemberRepository {
    Long getScoreByEmail(String email);
}
