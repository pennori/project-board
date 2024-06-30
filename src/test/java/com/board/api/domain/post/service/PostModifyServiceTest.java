package com.board.api.domain.post.service;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.request.PostModifyRequest;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;


@DisplayName("PostModifyService 테스트")
@SpringBootTest
class PostModifyServiceTest {

    @Autowired
    private PostModifyService postModifyService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private AuthorizationUtil authorizationUtil;



    @DisplayName("Post 수정시 예외 발생")
    @Test
    void modifyPostThrowPostException() {
        // given
        PostModifyRequest request = mock(PostModifyRequest.class);
        given(request.getPostId()).willReturn("1");

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> postModifyService.modifyPost(request))
                .isInstanceOf(PostException.class)
                .hasMessageContaining("Post 가 존재하지 않습니다.");

    }

    @DisplayName("Post 수정시 로그인 사용자와 작성자 간 불일치 예외 발생")
    @Test
    void modifyPostThrowPostExceptionCauseByLoginEmail() {
        // given
        PostModifyRequest request = mock(PostModifyRequest.class);
        given(request.getPostId()).willReturn("1");
        given(request.getTitle()).willReturn("title");
        given(request.getContent()).willReturn("content");

        Member member = mock(Member.class);
        given(member.getEmail()).willReturn("abcd@gmail.com");

        Post post = mock(Post.class);
        given(post.getPostId()).willReturn(1L);
        given(post.getMember()).willReturn(member);

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");

        // when then
        assertThatThrownBy(() -> postModifyService.modifyPost(request))
                .isInstanceOf(PostException.class)
                .hasMessageContaining("Post 에 대한 권한이 없습니다.");
    }

    @DisplayName("Post 수정")
    @Test
    void modifyPost() {
        // given
        PostModifyRequest request = mock(PostModifyRequest.class);
        given(request.getPostId()).willReturn("1");
        given(request.getTitle()).willReturn("title");
        given(request.getContent()).willReturn("content");

        Member member = mock(Member.class);
        given(member.getEmail()).willReturn("abc@gmail.com");

        Post post = mock(Post.class);
        given(post.getPostId()).willReturn(1L);
        given(post.getMember()).willReturn(member);

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");

        // when
        PostModifyDto dto = postModifyService.modifyPost(request);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPostId()).isEqualTo("1");
    }
}