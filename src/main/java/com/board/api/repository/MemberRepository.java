package com.board.api.repository;

import com.board.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomUserRepository {
    boolean existsByEmail(String email);

    Member findByEmail(String email);
}
