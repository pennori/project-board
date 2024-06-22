package com.board.api.repository;

import com.board.api.entity.Member;

public interface CustomUserRepository {
    Member getUserByEmail(String email);
}
