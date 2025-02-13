package com.board.api.global.security;

import com.board.api.domain.member.dto.MemberDto;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이메일 기준으로 Member 조회. 없으면 예외 발생.
        Member member = Optional.ofNullable(memberRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Member -> MemberDto 변환
        MemberDto memberDto = convertToMemberDto(member);

        // UserDetails 반환
        return new CustomUserDetails(memberDto);
    }

    // Member -> MemberDto 변환 로직을 추출
    private MemberDto convertToMemberDto(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getMemberRole().getName())
                .build();
    }
}