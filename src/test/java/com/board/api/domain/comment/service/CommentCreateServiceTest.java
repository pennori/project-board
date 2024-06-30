package com.board.api.domain.comment.service;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("CommentCreateService 테스트")
@SpringBootTest
@MockBeans({@MockBean(PointRepository.class), @MockBean(CommentRepository.class)})
class CommentCreateServiceTest {

    @Autowired
    private CommentCreateService commentCreateService;
    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AuthorizationUtil authorizationUtil;

    @DisplayName("Comment 생성시 Post 예외 발생")
    @Test
    void createCommentThrowPostException() {
        // given
        Member member = mock(Member.class);
        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");
        given(memberRepository.findByEmail(anyString())).willReturn(member);

        CommentRequest request = mock(CommentRequest.class);
        given(request.getPostId()).willReturn("1");
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> commentCreateService.createComment(request))
                .isInstanceOf(PostException.class)
                .hasMessageContaining("Post 가 존재하지 않습니다.");
    }

    @DisplayName("Comment 생성")
    @Test
    void createComment() {
        // given
        MemberPoint memberPoint = mock(MemberPoint.class);
        given(memberPoint.getScore()).willReturn(0L);

        Member member = mock(Member.class);
        given(member.getMemberId()).willReturn(1L);
        given(member.getMemberPoint()).willReturn(memberPoint);

        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");
        given(memberRepository.findByEmail(anyString())).willReturn(member);

        CommentRequest request = mock(CommentRequest.class);
        given(request.getPostId()).willReturn("1");

        Member memberFromPost = mock(Member.class);
        given(memberFromPost.getMemberId()).willReturn(2L);
        given(memberFromPost.getMemberPoint()).willReturn(memberPoint);

        Post post = mock(Post.class);
        given(post.getPostId()).willReturn(1L);
        given(post.getMember()).willReturn(memberFromPost);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        CommentDto dto = commentCreateService.createComment(request);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPostId()).isEqualTo("1");
    }

}