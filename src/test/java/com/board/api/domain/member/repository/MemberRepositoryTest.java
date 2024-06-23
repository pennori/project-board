package com.board.api.domain.member.repository;

import com.board.api.domain.member.entity.Member;
import com.board.api.global.config.QueryDSLConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

@DisplayName("MemberRepository 테스트")
@DataJpaTest
@Import({QueryDSLConfig.class})
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    void initData() {
        Member member =
                Member.builder()
                        .email("email@gmail.com")
                        .password("password")
                        .name("name")
                        .regNo("regNo")
                        .build();
        testEntityManager.persist(member);
    }

    @DisplayName("email 로 회원정보 존재 여부 조회")
    @Test
    void existsByEmail() {
        // given
        initData();

        // when
        boolean exists = memberRepository.existsByEmail("email@gmail.com");

        // then
        Assert.isTrue(exists, "email 로 존재 여부 조회 불가");
    }

    @DisplayName("email 로 회원정보 조회")
    @Test
    void findByEmail() {
        // given
        initData();

        // when
        Member member = memberRepository.findByEmail("email@gmail.com");

        // then
        Assert.isTrue(member.getMemberId() > 0L, "email 로 entity 조회 불가");
    }
}