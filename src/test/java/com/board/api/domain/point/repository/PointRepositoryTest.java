package com.board.api.domain.point.repository;


import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberRole;
import com.board.api.domain.point.entity.Point;
import com.board.api.global.config.QueryDSLConfig;
import com.board.api.global.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PointRepository 테스트")
@DataJpaTest
@Import({QueryDSLConfig.class})
class PointRepositoryTest {
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    Long initData() {
        Member member =
                Member.builder()
                        .email("email@gmail.com")
                        .password("password")
                        .name("name")
                        .regNo("regNo")
                        .build();

        MemberRole memberRole =
                MemberRole.builder()
                        .name(RoleType.USER.name())
                        .build();
        member.setMemberRole(memberRole);

        Point point = Point.builder().score(100L).build();
        member.setPoint(point);

        testEntityManager.persist(member);

        return member.getMemberId();
    }

    @DisplayName("memberId 로 현재 point 조회")
    @Test
    void getTotalPointByMemberId(){
        // given
        Long memberId = initData();

        // when
        Long totalPoint = pointRepository.getScoreByMemberId(memberId);

        // then
        assertThat(totalPoint).isEqualTo(100L);
    }
}