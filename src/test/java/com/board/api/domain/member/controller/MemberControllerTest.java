package com.board.api.domain.member.controller;

import com.board.api.domain.member.dto.SignUpDto;
import com.board.api.domain.member.dto.CurrentPointDto;
import com.board.api.domain.member.dto.request.SignUpRequest;
import com.board.api.domain.member.service.MemberPointService;
import com.board.api.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MemberController 테스트")
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberPointService memberPointService;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)) // REST Docs 설정 추가
                .defaultRequest(RestDocumentationRequestBuilders.get("/")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                                + "eyJzdWIiOiJ1c2VySWQiLCJleHAiOjE2MzI3MjM2MDAsImlhdCI6MTYzMjcyMDAwMH0."
                                + "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"))
                .build();
    }

    @Test
    @DisplayName("회원 가입 성공하고 200 OK를 반환한다.")
    void signUp_Success() throws Exception {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setEmail("testuser@example.com");
        request.setPassword("securePass1!");
        request.setName("Test User");
        request.setRegNo("700101-1234567");
        request.setRole("ADMIN");

        SignUpDto mockSignUpDto = SignUpDto.builder().memberId(1L).build();

        // when
        Mockito.when(memberService.createMember(any(SignUpRequest.class)))
                .thenReturn(mockSignUpDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.
                        post("/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("member-signup",
                        preprocessRequest(prettyPrint()), // 요청 전문을 보기 좋게 출력
                        preprocessResponse(prettyPrint()), // 응답 전문을 보기 좋게 출력
                        requestFields( // 요청 필드 문서화
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원의 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원의 비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원의 이름"),
                                fieldWithPath("regNo").type(JsonFieldType.STRING).description("회원의 주민등록번호"),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("회원의 권한")
                        ),
                        responseFields( // 응답 필드 문서화
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("data.memberId").type(JsonFieldType.STRING).description("회원 ID")
                        )
                ));
    }

    @Test
    @DisplayName("회원 포인트 조회 성공하고 200 OK를 반환한다.")
    void getMemberPoint_Success() throws Exception {
        // given
        CurrentPointDto mockCurrentPointDto = CurrentPointDto.builder().point(1000L).build();

        // when
        Mockito.when(memberPointService.getPoint()).thenReturn(mockCurrentPointDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/members/points")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-get-point",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("data.point").type(JsonFieldType.STRING).description("회원의 현재 포인트")
                        )
                ));
    }
}