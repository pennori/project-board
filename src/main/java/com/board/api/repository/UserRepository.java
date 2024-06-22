package com.board.api.repository;

import com.board.api.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>, CustomUserRepository {
    boolean existsByUserId(String userId);

    AppUser findByUserId(String userId);
}
