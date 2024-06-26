package com.board.api.domain.member.service;

import com.board.api.domain.member.dto.SignUp;
import com.board.api.domain.member.dto.request.SignUpRequest;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.entity.MemberRole;
import com.board.api.domain.member.exception.DuplicateException;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.global.constants.Author;
import com.board.api.global.encryption.BidirectionalCryptUtil;
import com.board.api.global.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public SignUp createMember(SignUpRequest request) throws Exception {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없음");
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if (exists) {
            throw new DuplicateException("존재하는 회원입니다.");
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
                        .name(RoleType.USER.name())
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberRole(memberRole);

        MemberPoint memberPoint =
                MemberPoint.builder()
                        .score(0L)
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberPoint(memberPoint);

        return SignUp.builder().memberId(member.getMemberId()).build();
    }
}