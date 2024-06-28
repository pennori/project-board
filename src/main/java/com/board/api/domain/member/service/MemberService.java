package com.board.api.domain.member.service;

import com.board.api.domain.member.dto.SignUpDto;
import com.board.api.domain.member.dto.request.SignUpRequest;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.entity.MemberRole;
import com.board.api.domain.member.exception.MemberException;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.global.constants.Author;
import com.board.api.global.encryption.BidirectionalCryptUtil;
import com.board.api.global.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BidirectionalCryptUtil cryptUtil;

    @Transactional
    public SignUpDto createMember(SignUpRequest request) throws Exception {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없습니다.");
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if (exists) {
            throw new MemberException("존재하는 회원입니다.");
        }

        if(!EnumUtils.isValidEnum(Role.class, request.getRole())) {
            throw new MemberException("유효한 권한이 아닙니다.");
        }

        Member member =
                Member.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .regNo(cryptUtil.encrypt(request.getRegNo()))
                        .createdBy(Author.SYSTEM_ID)
                        .build();

        memberRepository.save(member);

        MemberRole memberRole =
                MemberRole.builder()
                        .name(request.getRole())
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberRole(memberRole);

        MemberPoint memberPoint =
                MemberPoint.builder()
                        .score(0L)
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberPoint(memberPoint);

        return SignUpDto.builder().memberId(member.getMemberId()).build();
    }
}