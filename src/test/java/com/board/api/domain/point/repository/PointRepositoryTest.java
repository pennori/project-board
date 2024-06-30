package com.board.api.domain.point.repository;

import com.board.api.domain.point.entity.Point;
import com.board.api.domain.member.enums.Action;
import com.board.api.domain.member.enums.Category;
import com.board.api.global.config.QueryDSLConfig;
import com.board.api.global.constants.Author;
import com.board.api.global.util.QueryDSLUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PointHistoryRepository 테스트")
@DataJpaTest
@Import({QueryDSLConfig.class, QueryDSLUtil.class})
class PointRepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @DisplayName("Point 증감 이력 저장")
    @Test
    void save() {
        // given
        Point point =
                Point.builder()
                        .memberId(1L)
                        .postId(1L)
                        .commentId(0L)
                        .category(Category.POST.name())
                        .action(Action.CREATE.name())
                        .score(3L)
                        .createdBy(Author.SYSTEM_ID)
                        .build();

        // when
        pointRepository.save(point);

        //then
        assertThat(point.getPointId()).isGreaterThan(0L);
    }

}