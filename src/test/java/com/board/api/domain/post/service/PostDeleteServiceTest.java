package com.board.api.domain.post.service;

import com.board.api.domain.comment.entity.Comment;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;


@DisplayName("PostDeleteService 테스트")
@SpringBootTest
@MockBeans({@MockBean(PointRepository.class), @MockBean(CommentRepository.class)})
class PostDeleteServiceTest {
    @Autowired
    private PostDeleteService postDeleteService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AuthorizationUtil authorizationUtil;
    @DisplayName("Post 삭제시 예외 발생")
    @Test
    void deletePostThrowPostException() {
        // given
        long postId = 1L;

        // when then
        assertThatThrownBy(() -> postDeleteService.deletePost(postId)
        ).isInstanceOf(PostException.class)
                .hasMessageContaining("Post 가 존재하지 않습니다.");
    }

    @DisplayName("Post 삭제시 로그인 사용자와 작성자 간 불일치 예외 발생")
    @Test
    void deletePostThrowPostExceptionCauseByLoginEmail() {
        // given
        long postId = 1L;

        Member member = mock(Member.class);
        given(member.getEmail()).willReturn("abcd@gmail.com");

        Post post = mock(Post.class);
        given(post.getMember()).willReturn(member);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");

        /// when then
        assertThatThrownBy(() -> postDeleteService.deletePost(postId)
        ).isInstanceOf(PostException.class)
                .hasMessageContaining("Post 에 대한 권한이 없습니다.");
    }

    @DisplayName("Post 삭제")
    @Test
    void deletePost() {
        // given
        long postId = 1L;

        Member postMember = mock(Member.class);
        given(postMember.getEmail()).willReturn("abc@gmail.com");
        given(postMember.getMemberId()).willReturn(1L);

        Comment comment = mock(Comment.class);
        List<Comment> bunchOfComment = new ArrayList<>();
        bunchOfComment.add(comment);

        Member commentMember = mock(Member.class);
        given(commentMember.getMemberId()).willReturn(1L);
        given(comment.getMember()).willReturn(commentMember);

        Post post = mock(Post.class);
        given(post.getMember()).willReturn(postMember);
        given(post.getBunchOfComment()).willReturn(bunchOfComment);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        given(authorizationUtil.getLoginEmail()).willReturn("abc@gmail.com");

        MemberPoint memberPoint = mock(MemberPoint.class);
        given(memberPoint.getScore()).willReturn(2L);

        Member decreaseMember = mock(Member.class);
        given(decreaseMember.getMemberPoint()).willReturn(memberPoint);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(decreaseMember));

        // when
        postDeleteService.deletePost(postId);

        // then
        then(comment).should(times(1)).getCommentId();
    }
}