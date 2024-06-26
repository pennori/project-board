package com.board.api.domain.post.service;

import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.request.PostRequest;
import com.board.api.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostCreationDto createPost(PostRequest postRequest) {
        return null;
    }
}
