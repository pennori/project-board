package com.board.api.domain.post.repository;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.post.entity.Post;
import com.board.api.global.config.QueryDSLConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PostRepositoryTest")
@DataJpaTest
@Import({QueryDSLConfig.class})
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    TestEntityManager testEntityManager;

    void initMember(){
        Member member =
                Member.builder()
                        .email("email@gmail.com")
                        .password("password")
                        .name("name")
                        .regNo("regNo")
                        .build();

        testEntityManager.persist(member);
    }

    @DisplayName("Post 저장시 Post 만 insert")
    @Test
    void save(){
        // given
        initMember();

        // when
        Post post = Post.builder().content("content").build();
        Member member = memberRepository.findByEmail("email@gmail.com");
        post.setMember(member);

        postRepository.save(post);

        // then
        assertThat(post.getPostId()).isGreaterThan(0L);
        assertThat(post.getMember().getMemberId()).isEqualTo(member.getMemberId());
    }

}