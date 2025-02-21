package com.board.api.domain.comment.controller;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.service.CommentCreateService;
import com.board.api.domain.comment.service.CommentDeleteService;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
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
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CommentController 테스트")
@MockBeans({
        @MockBean(CommentDeleteService.class)
})
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentCreateService commentCreateService;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .defaultRequest(RestDocumentationRequestBuilders.get("/")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                                + "eyJzdWIiOiJ1c2VySWQiLCJleHAiOjE2MzI3MjM2MDAsImlhdCI6MTYzMjcyMDAwMH0."
                                + "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"))
                .build();
    }

    @Test
    @DisplayName("댓글을 성공적으로 생성하고 200 OK를 반환한다")
    void createComment() throws Exception {
        // given
        CommentRequest request = new CommentRequest();
        request.setPostId("1");
        request.setContent("This is a comment");

        CommentDto mockCommentDto = CommentDto.builder().postId(1L).commentId(1L).build();

        // when
        when(commentCreateService.createComment(any(CommentRequest.class))).thenReturn(mockCommentDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.resultMessage").value("OK"))
                .andDo(MockMvcRestDocumentationWrapper.document("comment-create-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
                        requestFields(
                                fieldWithPath("postId").type(JsonFieldType.STRING).description("댓글 작성할 게시물 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.postId").type(JsonFieldType.STRING).description("댓글 생성된 게시물 ID"),
                                fieldWithPath("data.commentId").type(JsonFieldType.STRING).description("생성된 댓글 ID")
                        )
                ));
    }

    @Test
    @DisplayName("댓글을 성공적으로 삭제한다")
    void deleteComment() throws Exception {
        // given
        Long commentId = 1L;

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("comment-delete-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
                        pathParameters(
                                parameterWithName("id").description("삭제할 댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("삭제 성공 시 데이터는 null")
                        )
                ));
    }
}