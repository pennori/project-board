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
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BidirectionalCryptUtil cryptUtil;
    private final MessageSource messageSource;

    @Transactional
    public SignUpDto createMember(SignUpRequest request) throws Exception {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없습니다.");
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if (exists) {
            throw new MemberException(messageSource.getMessage("exception.notexists", null, Locale.getDefault()));
        }

        if(!EnumUtils.isValidEnum(Role.class, request.getRole())) {
            throw new MemberException(messageSource.getMessage("exception.invalidrole", null, Locale.getDefault()));
        }

        Member member = saveMember(request);

        saveMemberRole(request, member);

        saveMemberPoint(member);

        return SignUpDto.builder().memberId(member.getMemberId()).build();
    }

    private void saveMemberPoint(Member member) {
        MemberPoint memberPoint =
                MemberPoint.builder()
                        .score(0L)
                        .createdBy(Author.SYSTEM_USER_ID)
                        .build();
        member.setMemberPoint(memberPoint);
    }

    private void saveMemberRole(SignUpRequest request, Member member) {
        MemberRole memberRole =
                MemberRole.builder()
                        .name(request.getRole())
                        .createdBy(Author.SYSTEM_USER_ID)
                        .build();
        member.setMemberRole(memberRole);
    }

    private Member saveMember(SignUpRequest request) throws Exception {
        Member member =
                Member.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .regNo(cryptUtil.encrypt(request.getRegNo()))
                        .createdBy(Author.SYSTEM_USER_ID)
                        .build();

        memberRepository.save(member);
        return member;
    }
}