package com.board.api.global.security;

import com.board.api.domain.member.dto.MemberDto;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);

        if(!ObjectUtils.isEmpty(member)) {
            // Entity 를 dto 에 정리
            MemberDto memberDto =
                    MemberDto.builder()
                            .memberId(member.getMemberId())
                            .email(member.getEmail())
                            .password(member.getPassword())
                            .role(member.getMemberRole().getName())
                            .build();
            // UserDetails에 담아서 return하면 AutneticationManager가 검증
            return new CustomUserDetails(memberDto);
        }

        return null;
    }
}
