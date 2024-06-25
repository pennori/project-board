package com.board.api.domain.member.repository;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberRole;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.global.config.QueryDSLConfig;
import com.board.api.global.constants.Author;
import com.board.api.global.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MemberRepository 테스트")
@DataJpaTest
@Import({QueryDSLConfig.class})
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TestEntityManager testEntityManager;


    @BeforeEach
    void initData() {
        Member member =
                Member.builder()
                        .email("email@gmail.com")
                        .password("password")
                        .name("name")
                        .regNo("regNo")
                        .createdBy(Author.SYSTEM_ID)
                        .build();

        MemberRole memberRole =
                MemberRole.builder()
                        .name(RoleType.USER.name())
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberRole(memberRole);

        MemberPoint memberPoint =
                MemberPoint.builder()
                        .score(1L)
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberPoint(memberPoint);

        testEntityManager.persist(member);
    }

    @DisplayName("email 로 회원정보 존재 여부 조회")
    @Test
    void existsByEmail() {
        // given

        // when
        boolean exists = memberRepository.existsByEmail("email@gmail.com");

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("email 로 회원정보 조회")
    @Test
    void findByEmail() {
        // given

        // when
        Member member = memberRepository.findByEmail("email@gmail.com");

        // then
        assertThat(member.getMemberId()).isGreaterThan(0L);
    }

}