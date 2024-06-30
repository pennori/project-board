package com.board.api.domain.comment.service;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.exception.CommentException;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;

@DisplayName("CommentService 테스트")
@SpringBootTest
@MockBeans(@MockBean(PointRepository.class))
class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CommentRepository commentRepository;

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
        assertThatThrownBy(() -> commentService.createComment(request))
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
        CommentDto dto = commentService.createComment(request);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPostId()).isEqualTo("1");
    }

    @DisplayName("Comment 삭제시 Comment 예외 발생")
    @Test
    void deleteCommentThrowCommentException() {
        // given
        Long commentId = 0L;

        // when then
        assertThatThrownBy(() -> commentService.deleteComment(commentId))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("Comment 가 존재하지 않습니다.");
    }

    @DisplayName("Comment 삭제시 로그인 사용자와 작성자 간 불일치 예외 발생")
    @Test
    void deleteCommentThrowCommentExceptionCauseByLoginEmail() {
        // given
        Long commentId = 0L;
        Optional<Comment> optionalComment = mock(Optional.class);
        given(commentRepository.findById(commentId)).willReturn(optionalComment);

        Comment comment = mock(Comment.class);
        given(optionalComment.get()).willReturn(comment);

        Member member = mock(Member.class);
        given(comment.getMember()).willReturn(member);
        given(member.getEmail()).willReturn("abcd@gmail.com");

        Post post = mock(Post.class);
        given(comment.getPost()).willReturn(post);

        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");

        // when then
        assertThatThrownBy(() -> commentService.deleteComment(commentId))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("Comment 에 대한 권한이 없습니다.");
    }

    @DisplayName("Comment 삭제")
    @Test
    void deleteComment() {
        // given
        Long commentId = 0L;
        Optional<Comment> optionalComment = mock(Optional.class);
        given(commentRepository.findById(commentId)).willReturn(optionalComment);

        Comment comment = mock(Comment.class);
        given(comment.getCommentId()).willReturn(1L);
        given(optionalComment.get()).willReturn(comment);

        MemberPoint memberPointFromMember = mock(MemberPoint.class);
        given(memberPointFromMember.getScore()).willReturn(0L);

        Member member = mock(Member.class);
        given(comment.getMember()).willReturn(member);
        given(member.getMemberId()).willReturn(1L);
        given(member.getEmail()).willReturn("abc@gmail.com");
        given(member.getMemberPoint()).willReturn(memberPointFromMember);

        MemberPoint memberPointFromPost = mock(MemberPoint.class);
        given(memberPointFromPost.getScore()).willReturn(0L);

        Member postMember = mock(Member.class);
        given(postMember.getMemberId()).willReturn(2L);
        given(postMember.getMemberPoint()).willReturn(memberPointFromPost);

        Post post = mock(Post.class);
        given(post.getPostId()).willReturn(1L);
        given(comment.getPost()).willReturn(post);
        given(post.getMember()).willReturn(postMember);

        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");

        // when
        commentService.deleteComment(commentId);

        // then
        then(comment).should(times(2)).getCommentId();
    }
}