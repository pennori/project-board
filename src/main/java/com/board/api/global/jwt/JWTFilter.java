package com.board.api.global.jwt;

import com.board.api.domain.member.dto.MemberDto;
import com.board.api.global.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token == null) {
            log.info("Token is not present or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtil.isTokenExpired(token)) {
            log.info("Token has expired");
            filterChain.doFilter(request, response);
            return;
        }

        authenticateUser(token);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTH_HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(BEARER_PREFIX.length()); // "Bearer " 부분 제거
    }

    private void authenticateUser(String token) {
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        MemberDto memberDto = MemberDto.builder()
                .email(username)
                .password("temppassword") // 실제로는 불필요하지만 요구사항에 맞추어 유지
                .role(role)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(memberDto);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User authenticated successfully: {}", username);
    }
}