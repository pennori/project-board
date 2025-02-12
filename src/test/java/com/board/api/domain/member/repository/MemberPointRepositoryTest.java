package com.board.api.domain.member.repository;


import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.entity.MemberRole;
import com.board.api.global.config.JPAQueryFactoryConfig;
import com.board.api.global.constants.Author;
import com.board.api.global.enums.Role;
import com.board.api.global.util.QueryDSLUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PointRepository 테스트")
@DataJpaTest
@Import({JPAQueryFactoryConfig.class, QueryDSLUtil.class})
class MemberPointRepositoryTest {
    @Autowired
    private MemberPointRepository memberPointRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    Long initData() {
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
                        .name(Role.USER.name())
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberRole(memberRole);

        MemberPoint memberPoint =
                MemberPoint.builder()
                        .score(100L)
                        .createdBy(Author.SYSTEM_ID)
                        .build();
        member.setMemberPoint(memberPoint);

        testEntityManager.persist(member);

        return member.getMemberId();
    }

    @DisplayName("memberId 로 현재 point 조회")
    @Test
    void getScoreByMemberId(){
        // given
        Long memberId = initData();

        // when
        Long totalPoint = memberPointRepository.getPoint(memberId);

        // then
        assertThat(totalPoint).isEqualTo(100L);
    }
}