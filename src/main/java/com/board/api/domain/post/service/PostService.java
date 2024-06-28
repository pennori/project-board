package com.board.api.domain.post.service;

import com.board.api.domain.comment.dto.CommentViewDto;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.enums.Action;
import com.board.api.domain.member.enums.Category;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.entity.PointHistory;
import com.board.api.domain.point.enums.PointType;
import com.board.api.domain.point.repository.PointHistoryRepository;
import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.PostModifyDto;
import com.board.api.domain.post.dto.PostViewDto;
import com.board.api.domain.post.dto.request.PostModifyRequest;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import io.jsonwebtoken.lang.Assert;
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
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CommentRepository commentRepository;
    private final AuthorizationUtil authorizationUtil;

    @Transactional
    public PostCreationDto createPost(PostCreateRequest request) {
        // post 저장
        Member member = memberRepository.findByEmail(authorizationUtil.getLoginEmail());
        Assert.notNull(member, "로그인한 회원의 요청이므로 회원정보가 존재해야 합니다.");

        Post post =
                Post.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .createdBy(member.getMemberId())
                        .build();
        post.setMember(member);
        postRepository.save(post);

        // point +3
        MemberPoint memberPoint = member.getMemberPoint();
        memberPoint.setScore(memberPoint.getScore() + PointType.CREATE_POST.getScore());
        memberPoint.setUpdatedBy(member.getMemberId());

        // point history 저장
        PointHistory pointHistory =
                PointHistory.builder()
                        .postId(post.getPostId())
                        .memberId(member.getMemberId())
                        .action(Action.CREATE.name())
                        .category(Category.POST.name())
                        .score(PointType.CREATE_POST.getScore())
                        .createdBy(member.getMemberId())
                        .build();
        pointHistoryRepository.save(pointHistory);

        return PostCreationDto.builder().postId(post.getPostId()).build();
    }

    @Transactional(readOnly = true)
    public PostViewDto viewPost(Long postId) {
        // Post
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isEmpty()) {
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

    @Transactional
    public PostModifyDto modifyPost(PostModifyRequest request) {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없습니다.");
        // Post
        Optional<Post> optionalPost = postRepository.findById(Long.valueOf(request.getPostId()));
        if(optionalPost.isEmpty()) {
            throw new PostException("Post 가 존재하지 않습니다.");
        }

        Post post = optionalPost.get();

        // 로그인 사용자가 글 작성자가 아니면 수정 불가
        if(!authorizationUtil.getLoginEmail().equals(post.getMember().getEmail())) {
            throw new PostException("Post 에 대한 권한이 없습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return PostModifyDto.builder()
                .postId(post.getPostId())
                .build();
    }

    @Transactional
    public void deletePost(Long postId) {
        // Post
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isEmpty()) {
            throw new PostException("Post 가 존재하지 않습니다.");
        }
        Post post = optionalPost.get();

        // comment 삭제
        List<Comment> bunchOfComment = post.getBunchOfComment();
        if(!ObjectUtils.isEmpty(bunchOfComment)) {
            commentRepository.deleteAll(bunchOfComment);
        }

        // post 삭제.
        postRepository.delete(post);

        // MemberPoint 조정

        // PointHistory 저장
    }
}
