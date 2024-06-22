package com.board.api.repository;

import com.board.api.entity.AppUser;

public interface CustomUserRepository {
    AppUser getUserByUserId(String userId);
}
