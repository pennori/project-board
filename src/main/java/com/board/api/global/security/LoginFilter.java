package com.board.api.global.security;

import com.board.api.domain.member.dto.LoginDto;
import com.board.api.domain.member.dto.request.LoginRequest;
import com.board.api.global.dto.response.ApiResponse;
import com.board.api.global.jwt.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;

        if (request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
            try {
                // ObjectMapper를 이용해서 JSON 데이터를 dto에 저장 후 dto의 데이터를 이용
                LoginRequest loginRequest =
                        objectMapper.readValue(
                                request
                                        .getReader()
                                        .lines()
                                        .collect(Collectors.joining()),
                                LoginRequest.class
                        );

                username = loginRequest.getEmail();
                password = loginRequest.getPassword();

                log.info("JSON 접속. email : {}, password : {}", username, password);
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            // POST 요청일 경우 기존과 같은 방식 이용
        } else if (HttpMethod.POST.name().equals(request.getMethod())) {
            // 클라이언트 요청에서 username, password 추출
            username = obtainUsername(request);
            password = obtainPassword(request);
        }

        // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token 내용을 AuthenticationManager 로 전달해서 검증
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //UserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, Long.parseLong(jwtUtil.getExpireTime()));

        // 성공시 Response Body 에 json 응답
        String result =
                objectMapper.writeValueAsString(
                        ApiResponse.<LoginDto>builder()
                                .status(HttpStatus.OK)
                                .data(LoginDto.builder().token(token).build())
                                .build()
                );
        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //로그인 실패시 401 응답 코드 반환
        String result =
                objectMapper.writeValueAsString(
                        ApiResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED)
                                .build()
                );
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(result);
    }
}
