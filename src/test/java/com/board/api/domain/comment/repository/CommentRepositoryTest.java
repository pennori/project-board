package com.board.api.domain.comment.repository;

import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.config.QueryDSLConfig;
import com.board.api.global.constants.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CommentRepository 테스트")
@DataJpaTest
@Import({QueryDSLConfig.class})
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Post initPost(){
        Member member =
                Member.builder()
                        .email("email@gmail.com")
                        .password("password")
                        .name("name")
                        .regNo("regNo")
                        .createdBy(Author.SYSTEM_ID)
                        .build();

        testEntityManager.persist(member);

        Post post =
                Post.builder()
                        .title("title")
                        .content("content")
                        .createdBy(member.getMemberId())
                        .build();
        post.setMember(member);

        testEntityManager.persist(post);

        return post;
    }

    @Test
    void save() {
        // given
        Post post = initPost();

        // when
        Comment comment =
                Comment.builder()
                        .content("comment")
                        .createdBy(post.getMember().getMemberId())
                        .build();
        comment.setMember(post.getMember());
        comment.setPost(post);

        commentRepository.save(comment);

        // then
        assertThat(comment.getCommentId()).isGreaterThan(0L);
        assertThat(comment.getPost().getPostId()).isGreaterThan(0L);
        assertThat(comment.getMember().getMemberId()).isGreaterThan(0L);
    }

}