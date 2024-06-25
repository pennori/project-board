package com.board.api.domain.member.repository;

public interface QMemberPointRepository {
    Long getScoreByMemberId(Long memberId);
}
