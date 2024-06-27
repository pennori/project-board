package com.board.api.domain.post.service;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.repository.PointHistoryRepository;
import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.request.PostRequest;
import com.board.api.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PointHistoryRepository pointHistoryRepository;

    @WithMockUser
    @Test
    void createPost() {
        // given
        Member member = mock(Member.class);
        given(memberRepository.findByEmail(anyString())).willReturn(member);
        given(member.getMemberId()).willReturn(1L);

        MemberPoint memberPoint = mock(MemberPoint.class);
        given(member.getMemberPoint()).willReturn(memberPoint);
        given(memberPoint.getScore()).willReturn(10L);

        PostRequest postRequest = mock(PostRequest.class);
        given(postRequest.getTitle()).willReturn("title");
        given(postRequest.getContent()).willReturn("content");

        // when
        PostCreationDto dto = postService.createPost(postRequest);

        // then
        assertThat(dto).isNotNull();
    }
}