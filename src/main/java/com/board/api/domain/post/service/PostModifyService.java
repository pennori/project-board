package com.board.api.domain.post.service;

import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.request.PostModifyRequest;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostModifyService {
    private final PostRepository postRepository;
    private final AuthorizationUtil authorizationUtil;

    @Transactional
    public PostModifyDto modifyPost(PostModifyRequest request) {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없습니다.");
        // Post
        Optional<Post> optionalPost = postRepository.findById(Long.valueOf(request.getPostId()));
        if (optionalPost.isEmpty()) {
            throw new PostException("Post 가 존재하지 않습니다.");
        }

        Post post = optionalPost.get();

        // 로그인 사용자가 글 작성자가 아니면 수정 불가
        if (!authorizationUtil.getLoginEmail().equals(post.getMember().getEmail())) {
            throw new PostException("Post 에 대한 권한이 없습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return PostModifyDto.builder()
                .postId(post.getPostId())
                .build();
    }

}
