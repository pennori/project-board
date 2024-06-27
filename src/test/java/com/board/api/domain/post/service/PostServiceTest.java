package com.board.api.domain.post.service;

import com.board.api.domain.comment.dto.CommentViewDto;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.repository.PointHistoryRepository;
import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    @MockBean
    private CommentRepository commentRepository;

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

        PostCreateRequest postCreateRequest = mock(PostCreateRequest.class);
        given(postCreateRequest.getTitle()).willReturn("title");
        given(postCreateRequest.getContent()).willReturn("content");

        // when
        PostCreationDto dto = postService.createPost(postCreateRequest);

        // then
        assertThat(dto).isNotNull();
    }

    @Test
    void viewPostThrowPostException() {
        // given
        Long postId = 0L;

        // when then
        assertThatThrownBy(() -> postService.viewPost(postId))
                .isInstanceOf(PostException.class)
                .hasMessageContaining("Post 가 존재하지 않습니다.");
    }

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
        PostViewDto postViewDto = postService.viewPost(postId);

        // then
        assertThat(postViewDto).isNotNull();
        assertThat(postViewDto.getPostId()).isEqualTo(String.valueOf(postId));
        assertThat(postViewDto.getBunchOfCommentViewDto()).isNotNull();
        assertThat(postViewDto.getBunchOfCommentViewDto().size()).isGreaterThan(0);
        assertThat(postViewDto.getBunchOfCommentViewDto().get(0)).isInstanceOf(CommentViewDto.class);
        assertThat(postViewDto.getBunchOfCommentViewDto().get(0).getCommentId()).isEqualTo(String.valueOf(commentId));
    }
}