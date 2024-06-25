package com.board.api.domain.member.service;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberPointRepository;
import com.board.api.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class MemberPointServiceTest {

    @Autowired
    private MemberPointService memberPointService;

    @MockBean
    private MemberPointRepository memberPointRepository;

    @MockBean
    private MemberRepository memberRepository;

    @WithMockUser
    @Test
    void getPoint() {
        // given
        Member member = mock(Member.class);
        given(member.getMemberId()).willReturn(Long.MAX_VALUE);
        given(memberRepository.findByEmail(anyString())).willReturn(member);

        long expected = 100L;
        given(memberPointRepository.getPoint(member.getMemberId())).willReturn(expected);

        // when
        Long point = memberPointService.getPoint();

        // then
        assertThat(point).isEqualTo(expected);
    }
}