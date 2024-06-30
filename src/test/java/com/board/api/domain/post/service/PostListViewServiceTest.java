package com.board.api.domain.post.service;

import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

@DisplayName("PostListViewService 테스트")
@SpringBootTest
@MockBeans(@MockBean(PointRepository.class))
class PostListViewServiceTest {

    @Autowired
    private PostListViewService postListViewService;

    @MockBean
    private PostRepository postRepository;

    @Test
    void getList() {
        // given
        Page<PostListViewDto> mockPage = mock(Page.class);
        given(mockPage.getTotalElements()).willReturn(10L);

        Pageable pageable = PageRequest.of(0, 10);
        given(postRepository.getList(pageable)).willReturn(mockPage);

        // when
        Page<PostListViewDto> dto = postListViewService.listViewPost(pageable);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getTotalElements()).isEqualTo(10L);
    }
}