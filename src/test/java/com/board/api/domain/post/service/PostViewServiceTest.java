package com.board.api.domain.post.service;

import com.board.api.domain.comment.dto.CommentViewDto;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;


@DisplayName("PostViewService 테스트")
@SpringBootTest
class PostViewServiceTest {

    @Autowired
    private PostViewService postViewService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("Post 조회시 예외 발생")
    @Test
    void viewPostThrowPostException() {
        // given
        Long postId = 0L;

        // when then
        assertThatThrownBy(() -> postViewService.viewPost(postId))
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
        PostViewDto dto = postViewService.viewPost(postId);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPostId()).isEqualTo(String.valueOf(postId));
        assertThat(dto.getBunchOfCommentViewDto()).isNotNull();
        assertThat(dto.getBunchOfCommentViewDto().size()).isGreaterThan(0);
        assertThat(dto.getBunchOfCommentViewDto().get(0)).isInstanceOf(CommentViewDto.class);
        assertThat(dto.getBunchOfCommentViewDto().get(0).getCommentId()).isEqualTo(String.valueOf(commentId));
    }

}