package com.board.api.domain.member.service;

import com.board.api.domain.member.dto.CurrentPointDto;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberPointRepository;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.global.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("MemberPointService 테스트")
@SpringBootTest
class MemberPointServiceTest {

    @Autowired
    private MemberPointService memberPointService;

    @MockBean
    private MemberPointRepository memberPointRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AuthorizationUtil authorizationUtil;

    @DisplayName("로그인 사용자의 현재 포인트 조회")
    @WithMockUser
    @Test
    void getPoint() {
        // given
        Member member = mock(Member.class);
        long memberId = 1L;
        given(member.getMemberId()).willReturn(memberId);
        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");
        given(memberRepository.findByEmail(anyString())).willReturn(member);

        long point = 100L;
        given(memberPointRepository.getPoint(member.getMemberId())).willReturn(point);

        // when
        CurrentPointDto currentPointDto = memberPointService.getPoint();

        // then
        String expected = String.valueOf(point);
        assertThat(currentPointDto.getPoint()).isEqualTo(expected);
    }
}