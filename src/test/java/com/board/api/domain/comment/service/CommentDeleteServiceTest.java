package com.board.api.domain.comment.service;

import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.exception.CommentException;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.post.entity.Post;
import com.board.api.global.util.AuthorizationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@DisplayName("CommentDeleteService 테스트")
@SpringBootTest
class CommentDeleteServiceTest {

    @Autowired
    private CommentDeleteService commentDeleteService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AuthorizationUtil authorizationUtil;

    @DisplayName("Comment 삭제시 Comment 예외 발생")
    @Test
    void deleteCommentThrowCommentException() {
        // given
        Long commentId = 0L;

        // when then
        assertThatThrownBy(() -> commentDeleteService.deleteComment(commentId))
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
        assertThatThrownBy(() -> commentDeleteService.deleteComment(commentId))
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
        commentDeleteService.deleteComment(commentId);

        // then
        then(comment).should(times(2)).getCommentId();
    }
}