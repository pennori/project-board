package com.board.api.domain.post.repository;

import com.board.api.domain.post.dto.PostListViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QPostRepository {
    Page<PostListViewDto> getList(Pageable pageable);
}
