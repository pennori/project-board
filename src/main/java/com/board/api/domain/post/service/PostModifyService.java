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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostModifyService {
    private final PostRepository postRepository;
    private final AuthorizationUtil authorizationUtil;
    private final MessageSource messageSource;

    @CacheEvict(value = "board", allEntries = true)
    @Transactional
    public PostModifyDto modifyPost(PostModifyRequest request) {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없습니다.");
        // Post
        Optional<Post> optionalPost = postRepository.findById(Long.valueOf(request.getPostId()));
        if (optionalPost.isEmpty()) {
            throw new PostException(messageSource.getMessage("exception.notfound", new String[]{"Post"}, Locale.getDefault()));
        }

        Post post = optionalPost.get();

        // 로그인 사용자가 글 작성자가 아니면 수정 불가
        if (!authorizationUtil.getLoginEmail().equals(post.getMember().getEmail())) {
            throw new PostException(messageSource.getMessage("exception.unauthorized", new String[]{"Post"}, Locale.getDefault()));
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return PostModifyDto.builder()
                .postId(post.getPostId())
                .build();
    }

}
