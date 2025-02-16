package com.board.api.domain.post.controller;

import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostCreateService postCreateService;

    @MockBean
    private PostModifyService postModifyService;

    @MockBean
    private PostViewService postViewService;

    @MockBean
    private PostListViewService postListViewService;

    @MockBean
    private PostDeleteService postDeleteService;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("게시물을 성공적으로 생성하고 200 OK를 반환한다")
    void createPost_Success() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("샘플 제목");
        request.setContent("샘플 내용");

        PostCreationDto responseDto = PostCreationDto.builder().postId(1L).build();
        Mockito.when(postCreateService.createPost(any(PostCreateRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.resultMessage").value("OK"))
                .andDo(document("post-create-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.postId").type(JsonFieldType.STRING).description("생성된 게시물 ID")
                        )
                ));
    }

    @Test
    @DisplayName("제목이 비어 있으면 400 Bad Request를 반환한다")
    void createPost_Failure_BlankTitle() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("샘플 제목");
        request.setContent("");

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-blank-content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용 (필수)")
                        ),
                        // 누락된 responseFields에 data.content 추가
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("유효성 검사 메시지: 내용의 길이가 유효하지 않습니다.")
                        )
                ));
    }

    @Test
    @DisplayName("내용이 비어 있으면 400 Bad Request를 반환한다")
    void createPost_Failure_BlankContent() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("샘플 제목");
        request.setContent(""); // 내용이 비어있는 요청으로 테스트 진행

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-blank-content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용 (필수)")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.content")
                                        .type(JsonFieldType.STRING)
                                        .description("유효성 검사 메시지: 내용의 길이가 유효하지 않습니다.") // 누락된 필드 추가
                        )
                ));
    }

    @Test
    @DisplayName("제목이 최대 길이를 초과하면 400 Bad Request를 반환한다")
    void createPost_Failure_TitleTooLong() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("A".repeat(256));
        request.setContent("샘플 내용");

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-title-too-long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("게시물 제목 (길이 255자 이내)"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("게시물 내용")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.title")
                                        .type(JsonFieldType.STRING)
                                        .description("유효성 검사 메시지: 제목의 길이가 너무 깁니다.")
                        )
                ));
    }

    @Test
    @DisplayName("내용이 최대 길이를 초과하면 400 Bad Request를 반환한다")
    void createPost_Failure_ContentTooLong() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("샘플 제목");
        request.setContent("A".repeat(256));

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-content-too-long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용 (길이 255자 이내)")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("유효성 검사 메시지: 내용의 길이가 유효하지 않습니다.")
                        )
                ));
    }
}