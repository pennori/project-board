package com.board.api.domain.post.service;

import com.board.api.domain.post.dto.PostListViewDto;
import com.board.api.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostListViewService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostListViewDto> listViewPost(Pageable pageable) {
        return postRepository.getList(pageable);
    }
}
