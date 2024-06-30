package com.board.api.domain.post.service;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;


@DisplayName("PostCreateService 테스트")
@SpringBootTest
@MockBeans({@MockBean(PointRepository.class), @MockBean(PostRepository.class)})
class PostCreateServiceTest {

    @Autowired
    private PostCreateService postCreateService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AuthorizationUtil authorizationUtil;

    @DisplayName("Post 생성")
    @WithMockUser
    @Test
    void createPost() {
        // given
        Member member = mock(Member.class);
        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");
        given(memberRepository.findByEmail(anyString())).willReturn(member);
        given(member.getMemberId()).willReturn(1L);

        MemberPoint memberPoint = mock(MemberPoint.class);
        given(member.getMemberPoint()).willReturn(memberPoint);
        given(memberPoint.getScore()).willReturn(10L);

        PostCreateRequest postCreateRequest = mock(PostCreateRequest.class);
        given(postCreateRequest.getTitle()).willReturn("title");
        given(postCreateRequest.getContent()).willReturn("content");

        // when
        PostCreationDto dto = postCreateService.createPost(postCreateRequest);

        // then
        assertThat(dto).isNotNull();
    }

}