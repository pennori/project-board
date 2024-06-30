package com.board.api.domain.post.service;

import com.board.api.domain.comment.dto.CommentViewDto;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostViewService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PostViewDto viewPost(Long postId) {
        // Post
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new PostException("Post 가 존재하지 않습니다.");
        }
        Post post = optionalPost.get();

        // CommentViewDto init
        List<CommentViewDto> bunchOfCommentViewDto = new ArrayList<>();

        // Comment into bunchOfCommentViewDto
        List<Comment> bunchOfComment = commentRepository.getBunchOfComment(postId);
        if (!ObjectUtils.isEmpty(bunchOfComment)) {
            for (Comment comment : bunchOfComment) {
                CommentViewDto commentViewDto =
                        CommentViewDto.builder()
                                .commentId(comment.getCommentId())
                                .content(comment.getContent())
                                .updatedAt(comment.getUpdatedAt())
                                .updatedBy(comment.getUpdatedBy())
                                .build();
                bunchOfCommentViewDto.add(commentViewDto);
            }
        }

        // bunchOfCommentViewDto into PostViewDto
        return PostViewDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .updatedBy(post.getUpdatedBy())
                .bunchOfCommentViewDto(bunchOfCommentViewDto)
                .build();
    }

}
