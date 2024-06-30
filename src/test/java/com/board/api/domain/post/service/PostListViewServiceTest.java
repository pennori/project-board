package com.board.api.domain.post.service;

import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;


@DisplayName("PostListViewService 테스트")
@SpringBootTest
@MockBeans(@MockBean(PointRepository.class))
class PostListViewServiceTest {

    @Autowired
    private PostListViewService postListViewService;

    @MockBean
    private PostRepository postRepository;

}