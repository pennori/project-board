package com.board.api.domain.member.repository;

public interface CustomMemberRepository {
    Long getPointByEmail(String email);
}
