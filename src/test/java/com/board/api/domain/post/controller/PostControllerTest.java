package com.board.api.domain.post.controller;

import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.dto.request.PostModifyRequest;
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
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
        @MockBean(PostDeleteService.class)
})
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
    @DisplayName("게시물을 성공적으로 생성하고 200 OK를 반환한다")
    void createPost_Success() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("샘플 제목");
        request.setContent("샘플 내용");

        PostCreationDto responseDto = PostCreationDto.builder().postId(1L).build();
        Mockito.when(postCreateService.createPost(any(PostCreateRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.resultMessage").value("OK"))
                .andDo(document("post-create-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
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

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-blank-content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
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

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-blank-content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
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

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-title-too-long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
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

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("post-create-failure-content-too-long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
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

    @Test
    @DisplayName("게시물 정보를 성공적으로 조회한다")
    void getPost_Success() throws Exception {
        long postId = 1L;

        Mockito.when(postViewService.viewPost(postId))
                .thenReturn(PostViewDto.builder()
                        .postId(postId)
                        .title("샘플 제목")
                        .content("샘플 내용")
                        .updatedAt(LocalDateTime.parse("2025-02-16T12:00:00"))
                        .updatedBy(1L) // Replace "수정자" with a Long value, e.g., a user ID.
                        .bunchOfCommentViewDto(null)
                        .build());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-get-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
                        pathParameters(
                                parameterWithName("id").description("조회할 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.postId").type(JsonFieldType.STRING).description("게시물 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 시각"),
                                fieldWithPath("data.updatedBy").type(JsonFieldType.STRING).description("수정자"),
                                fieldWithPath("data.bunchOfCommentViewDto").type(JsonFieldType.NULL).description("댓글 정보 (null일 수 있음)")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 정보를 성공적으로 수정한다")
    void modifyPost_Success() throws Exception {
        // Given
        PostModifyRequest modifyRequest = new PostModifyRequest();
        modifyRequest.setPostId(String.valueOf(1L)); // postId를 요청 본문에 포함
        modifyRequest.setTitle("수정된 제목");
        modifyRequest.setContent("수정된 내용");

        // Mock Service Layer
        PostModifyDto mockResponse = PostModifyDto.builder()
                .postId(1L) // 반환될 게시물 ID
                .build();
        Mockito.when(postModifyService.modifyPost(any(PostModifyRequest.class)))
                .thenReturn(mockResponse); // 반환 값을 설정

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/posts") // "/post"로 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequest))) // 요청 본문에 JSON 데이터 포함
                .andExpect(status().isOk()) // 200 상태 확인
                .andExpect(jsonPath("$.resultCode").value("200")) // 응답 성공 코드 확인
                .andExpect(jsonPath("$.data.postId").value(1)) // 반환된 postId 확인
                .andDo(document("post-modify-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
                        requestFields(
                                fieldWithPath("postId").type(JsonFieldType.STRING).description("수정할 게시물 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 게시물 내용")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.postId").type(JsonFieldType.STRING).description("수정된 게시물 ID")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 목록을 성공적으로 조회한다")
    void listPosts_Success() throws Exception {
        // Mock 데이터 준비
        PageImpl<PostListViewDto> mockPageData = new PageImpl<>(List.of(
                PostListViewDto.builder()
                        .postId(1L)
                        .title("샘플 제목 1")
                        .build(),
                PostListViewDto.builder()
                        .postId(2L)
                        .title("샘플 제목 2")
                        .build()
        ));
        Mockito.when(postListViewService.listViewPost(any()))
                .thenReturn(mockPageData);

        // MockMvc를 통한 요청 테스트
        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts")
                .param("page", "1")
                .param("size", "10")
                .param("sort","id,desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-list-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
                        queryParameters(
                                parameterWithName("page").description("조회할 게시물 페이지 번호").optional(),
                                parameterWithName("size").description("조회할 게시물 전체 페이지 사이즈").optional(),
                                parameterWithName("sort").description("조회할 게시물 정렬 키와 정렬 방식 배열").optional()
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.content[].postId").type(JsonFieldType.STRING).description("게시물 ID"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 게시물 수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("비어 있는 페이지 여부"),
                                fieldWithPath("data.content[].content").optional().type(JsonFieldType.STRING).description("게시물 내용 (nullable)"),
                                fieldWithPath("data.content[].updatedAt").optional().type(JsonFieldType.STRING).description("수정된 날짜 (nullable)"),
                                fieldWithPath("data.content[].updatedBy").optional().type(JsonFieldType.STRING).description("수정자 (nullable)"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어 있는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않은 상태 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬된 상태 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 게시물 수"),
                                fieldWithPath("data.pageable").optional().type(JsonFieldType.STRING).description("페이징 정보 (INSTANCE)").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("게시물을 성공적으로 삭제한다")
    void deletePost_Success() throws Exception {
        long postId = 1L;

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-delete-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 형식의 JWT 토큰을 포함한 인증 헤더 (실제 token 입력은 각 End Point 우측 자물쇠 아이콘 혹은 문서 최상단 Authorization 버튼 클릭 후 입력)")
                        ),
                        pathParameters(
                                parameterWithName("id").description("삭제할 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resultMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("삭제 성공 시 데이터는 null")
                        )
                ));
    }

}