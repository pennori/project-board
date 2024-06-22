package com.board.api.domain.member.repository;

import com.board.api.domain.member.entity.Member;

public interface CustomMemberRepository {
    Member getMemberByEmail(String email);
}
