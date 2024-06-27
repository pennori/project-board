package com.board.api.domain.member.service;

import com.board.api.domain.member.dto.CurrentPointDto;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberPointRepository;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.global.util.AuthorizationUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPointService {
    private final MemberRepository memberRepository;
    private final MemberPointRepository memberPointRepository;

    public CurrentPointDto getPoint() {
        Member member = memberRepository.findByEmail(AuthorizationUtil.getLoginEmail());
        Assert.notNull(member, "로그인한 회원의 요청이므로 회원정보가 존재해야 합니다.");

        Long point = memberPointRepository.getPoint(member.getMemberId());

        return CurrentPointDto.builder().point(point).build();
    }
}
