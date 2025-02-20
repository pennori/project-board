package com.board.api.global.security;

import com.board.api.domain.member.dto.MemberDto;
import com.board.api.domain.member.dto.request.LoginRequest;
import com.board.api.global.jwt.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class LoginFilterTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("회원 로그인 성공 테스트")
    public void loginEndpointTest() throws Exception {
        // 테스트에 사용할 사용자 정보 설정
        MemberDto memberDto = MemberDto.builder()
                .email("testuser@example.com")
                .password("securePass1!")
                .role("ADMIN")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(memberDto);

        LoginRequest request = new LoginRequest();
        request.setUserId("testuser@example.com");
        request.setPassword("securePass1!");

        // AuthenticationManager.authenticate() 메서드가 성공적으로 인증을 반환하도록 모킹
        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        // JWTUtil과 AuthenticationManager의 동작을 모킹
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzQwMDU4OTAwLCJleHAiOjE3NDAwNjA3MDB9.K3EmX8G-VGlWapnuUDgy1782gSaE5mziDyfFXX4DGJo";
        when(jwtUtil.createJwt(anyString(), anyString(), anyLong())).thenReturn(token);
        when(jwtUtil.getExpireTime()).thenReturn("1800000");

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        mockMvc.perform(RestDocumentationRequestBuilders.
                        post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("member-login",
                        preprocessRequest(prettyPrint()), // 요청 전문을 보기 좋게 출력
                        preprocessResponse(prettyPrint()), // 응답 전문을 보기 좋게 출력
                        requestFields( // 요청 필드 문서화
                                fieldWithPath("userId").description("회원의 아이디 (이메일)"),
                                fieldWithPath("password").description("회원의 비밀번호")
                        ),
                        responseFields( // 응답 필드 문서화
                                fieldWithPath("resultCode").description("결과 코드"),
                                fieldWithPath("resultMessage").description("결과 메시지"),
                                fieldWithPath("data.token").description("회원 JWT")
                        )
                ));

        // 모킹된 메소드 검증
        verify(jwtUtil, times(1)).createJwt(anyString(), anyString(), anyLong());
    }
}
