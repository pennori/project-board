package com.board.api.domain.member.repository;

import com.board.api.domain.member.entity.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPointRepository extends JpaRepository<MemberPoint, Long>, QMemberPointRepository {
}
