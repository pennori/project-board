package com.board.api.domain.post.repository;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.entity.Post;
import com.board.api.global.config.QueryDSLConfig;
import com.board.api.global.constants.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

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

    Member initMember() {
        Member member =
                Member.builder()
                        .email("email@gmail.com")
                        .password("password")
                        .name("name")
                        .regNo("regNo")
                        .createdBy(Author.SYSTEM_ID)
                        .build();

        testEntityManager.persist(member);

        return member;
    }

    void initPost(int qty) {
        Member member = initMember();

        for(int i = 0; i < qty; i++) {
            Post post = Post.builder().title("title").content("content").createdBy(member.getMemberId()).build();
            post.setMember(member);

            testEntityManager.persist(post);
        }
    }

    @DisplayName("Post 저장시 Post 만 insert")
    @Test
    void save() {
        // given
        initMember();

        // when
        Member member = memberRepository.findByEmail("email@gmail.com");
        Post post = Post.builder().title("title").content("content").createdBy(member.getMemberId()).build();
        post.setMember(member);

        postRepository.save(post);

        // then
        assertThat(post.getPostId()).isGreaterThan(0L);
        assertThat(post.getMember().getMemberId()).isEqualTo(member.getMemberId());
    }

    @DisplayName("Post 목록 조회")
    @Test
    void getList(){
        // given
        initPost(20);

        // when
        Pageable pageable = PageRequest.of(1, 10, Sort.Direction.DESC, "post_id");
        Page<PostListViewDto> page = postRepository.getList(pageable);

        // then
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(20);
        assertThat(page.getContent().get(0)).isInstanceOf(PostListViewDto.class);
        assertThat(page.getContent().get(0).getTitle()).isEqualTo("title");
        System.out.println("========" + page.getContent().get(0).getPostId());
    }

}