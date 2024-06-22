package com.board.api.service;

import com.board.api.dto.SignUpRequest;
import com.board.api.encryption.BidirectionalCryptUtil;
import com.board.api.entity.AppUser;
import com.board.api.entity.UserRole;
import com.board.api.enums.RoleType;
import com.board.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BidirectionalCryptUtil cryptUtil;

    @Transactional(propagation = Propagation.REQUIRED)
    public long saveUser(SignUpRequest request) throws Exception {
        boolean exists = userRepository.existsByUserId(request.getUserId());
        if (exists) {
            return 0L;
        }

        UserRole userRole =
                UserRole.builder()
                        .name(RoleType.USER.name())
                        .build();
        AppUser appUser =
                AppUser.builder()
                        .userId(request.getUserId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .idType(request.getIdType())
                        .idValue(cryptUtil.encryptAES256(request.getIdValue()))
                        .build();
        appUser.setUserRole(userRole);
        userRepository.save(appUser);

        return appUser.getUserSeq();
    }
}