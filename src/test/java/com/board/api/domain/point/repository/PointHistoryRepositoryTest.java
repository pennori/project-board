package com.board.api.domain.point.repository;

import com.board.api.domain.point.entity.PointHistory;
import com.board.api.domain.point.enums.Action;
import com.board.api.domain.point.enums.Category;
import com.board.api.global.config.QueryDSLConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PointHistoryRepository 테스트")
@DataJpaTest
@Import({QueryDSLConfig.class})
class PointHistoryRepositoryTest {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    Long initData() {
        return 0L;
    }

    @DisplayName("Point 증감 이력 저장")
    @Test
    void save() {
        // given
        PointHistory pointHistory =
                PointHistory.builder()
                        .memberId(1L)
                        .postId(1L)
                        .commentId(0L)
                        .category(Category.POST.name())
                        .action(Action.CREATE.name())
                        .score(3L)
                        .build();

        // when
        pointHistoryRepository.save(pointHistory);

        //then
        assertThat(pointHistory.getPointHistoryId()).isGreaterThan(0L);
    }

}