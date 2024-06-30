package com.board.api.domain.post.service;

import com.board.api.domain.comment.dto.CommentViewDto;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
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
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@DisplayName("PostService 테스트")
@SpringBootTest
@MockBeans(@MockBean(PointRepository.class))
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CommentRepository commentRepository;

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
        PostCreationDto dto = postService.createPost(postCreateRequest);

        // then
        assertThat(dto).isNotNull();
    }

    @DisplayName("Post 조회시 예외 발생")
    @Test
    void viewPostThrowPostException() {
        // given
        Long postId = 0L;

        // when then
        assertThatThrownBy(() -> postService.viewPost(postId))
                .isInstanceOf(PostException.class)
                .hasMessageContaining("Post 가 존재하지 않습니다.");
    }

    @DisplayName("Post 조회")
    @Test
    void viewPost() {
        // given
        Long postId = 1L;
        Long updatedBy = 2L;
        Long commentId = 3L;

        Post post = mock(Post.class);
        given(post.getPostId()).willReturn(postId);
        given(post.getTitle()).willReturn("title");
        given(post.getContent()).willReturn("content");
        given(post.getUpdatedAt()).willReturn(LocalDateTime.now());
        given(post.getUpdatedBy()).willReturn(updatedBy);

        Comment comment = mock(Comment.class);
        given(comment.getCommentId()).willReturn(commentId);
        given(comment.getContent()).willReturn("content");
        given(comment.getUpdatedAt()).willReturn(LocalDateTime.now());
        given(comment.getUpdatedBy()).willReturn(postId);

        Optional<Post> optionalPost = Optional.of(post);
        given(postRepository.findById(postId)).willReturn(optionalPost);

        ArrayList<Comment> bunchOfComment = new ArrayList<>();
        bunchOfComment.add(comment);
        given(commentRepository.getBunchOfComment(postId)).willReturn(bunchOfComment);

        // when
        PostViewDto dto = postService.viewPost(postId);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPostId()).isEqualTo(String.valueOf(postId));
        assertThat(dto.getBunchOfCommentViewDto()).isNotNull();
        assertThat(dto.getBunchOfCommentViewDto().size()).isGreaterThan(0);
        assertThat(dto.getBunchOfCommentViewDto().get(0)).isInstanceOf(CommentViewDto.class);
        assertThat(dto.getBunchOfCommentViewDto().get(0).getCommentId()).isEqualTo(String.valueOf(commentId));
    }

    @DisplayName("Post 수정시 예외 발생")
    @Test
    void modifyPostThrowPostException() {
        // given
        PostModifyRequest request = mock(PostModifyRequest.class);
        given(request.getPostId()).willReturn("1");

        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> postService.modifyPost(request))
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
        assertThatThrownBy(() -> postService.modifyPost(request))
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
        PostModifyDto dto = postService.modifyPost(request);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPostId()).isEqualTo("1");
    }

    @DisplayName("Post 삭제시 예외 발생")
    @Test
    void deletePostThrowPostException() {
        // given
        long postId = 1L;

        // when then
        assertThatThrownBy(() -> postService.deletePost(postId)
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
        assertThatThrownBy(() -> postService.deletePost(postId)
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
        postService.deletePost(postId);

        // then
        then(comment).should(times(1)).getCommentId();
    }
}