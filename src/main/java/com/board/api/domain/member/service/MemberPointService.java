package com.board.api.domain.member.service;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberPointRepository;
import com.board.api.domain.member.repository.MemberRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPointService {
    private final MemberRepository memberRepository;
    private final MemberPointRepository memberPointRepository;

    public Long getPoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);
        Assert.notNull(member, "로그인한 회원의 요청이므로 회원정보가 존재해야 함");

        return memberPointRepository.getPoint(member.getMemberId());
    }
}
