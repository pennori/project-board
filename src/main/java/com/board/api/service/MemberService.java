package com.board.api.service;

import com.board.api.dto.SignUpRequest;
import com.board.api.encryption.BidirectionalCryptUtil;
import com.board.api.entity.Member;
import com.board.api.entity.MemberRole;
import com.board.api.enums.RoleType;
import com.board.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BidirectionalCryptUtil cryptUtil;

    @Transactional(propagation = Propagation.REQUIRED)
    public long saveMember(SignUpRequest request) throws Exception {
        Assert.notNull(request, HttpStatus.BAD_REQUEST.name());
        boolean exists = memberRepository.existsByEmail(request.getUserId());
        if (exists) {
            return 0L;
        }

        MemberRole memberRole =
                MemberRole.builder()
                        .name(RoleType.USER.name())
                        .build();
        Member member =
                Member.builder()
                        .email(request.getUserId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .idType(request.getIdType())
                        .idValue(cryptUtil.encryptAES256(request.getIdValue()))
                        .build();
        member.setMemberRole(memberRole);
        memberRepository.save(member);

        return member.getMemberId();
    }
}