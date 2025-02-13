package com.board.api.global.security;

import com.board.api.domain.member.dto.LoginDto;
import com.board.api.domain.member.dto.request.LoginRequest;
import com.board.api.global.dto.response.ApiResponse;
import com.board.api.global.jwt.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;

        if (MimeTypeUtils.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType())) {
            LoginRequest loginRequest = parseJsonRequest(request);
            if (loginRequest == null) {
                throw new IllegalArgumentException("Invalid login request");
            }
            username = loginRequest.getUserId();
            password = loginRequest.getPassword();
        } else if (HttpMethod.POST.name().equals(request.getMethod())) {
            username = obtainUsername(request);
            password = obtainPassword(request);
        } else {
            throw new UnsupportedOperationException("Unsupported authentication method");
        }

        log.info("Authentication attempt. Username: {}", username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    private LoginRequest parseJsonRequest(HttpServletRequest request) {
        try {
            String json = request.getReader().lines().collect(Collectors.joining());
            return objectMapper.readValue(json, LoginRequest.class);
        } catch (IOException e) {
            log.error("Error parsing JSON request: {}", e.getMessage());
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String token = jwtUtil.createJwt(
                userDetails.getUsername(),
                authResult.getAuthorities().iterator().next().getAuthority(),
                Long.parseLong(jwtUtil.getExpireTime())
        );

        setSuccessResponse(response, token);
    }

    private void setSuccessResponse(HttpServletResponse response, String token) throws IOException {
        String result = objectMapper.writeValueAsString(
                ApiResponse.<LoginDto>builder()
                        .status(HttpStatus.OK)
                        .data(LoginDto.builder().token(token).build())
                        .build()
        );
        response.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        setFailureResponse(response);
    }

    private void setFailureResponse(HttpServletResponse response) throws IOException {
        String result = objectMapper.writeValueAsString(
                ApiResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .build()
        );
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(result);
    }
}