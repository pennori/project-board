package com.board.api.domain.member.service;

import com.board.api.domain.member.dto.request.SignUpRequest;
import com.board.api.domain.point.entity.Point;
import com.board.api.global.encryption.BidirectionalCryptUtil;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberRole;
import com.board.api.global.enums.RoleType;
import com.board.api.domain.member.repository.MemberRepository;
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
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if (exists) {
            return 0L;
        }

        Member member =
                Member.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .regNo(cryptUtil.encrypt(request.getRegNo()))
                        .build();

        memberRepository.save(member);

        MemberRole memberRole =
                MemberRole.builder()
                        .name(RoleType.USER.name())
                        .build();
        member.setMemberRole(memberRole);

        Point point = new Point(0L);
        member.setPoint(point);

        return member.getMemberId();
    }
}