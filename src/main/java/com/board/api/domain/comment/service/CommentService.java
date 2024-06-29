package com.board.api.domain.comment.service;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.exception.CommentException;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.enums.Action;
import com.board.api.domain.member.enums.Category;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.entity.PointHistory;
import com.board.api.domain.point.enums.PointType;
import com.board.api.domain.point.repository.PointHistoryRepository;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final AuthorizationUtil authorizationUtil;

    public CommentDto createComment(CommentRequest request) {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없습니다.");

        Member member = memberRepository.findByEmail(authorizationUtil.getLoginEmail());
        Assert.notNull(member, "로그인한 회원의 요청이므로 회원정보가 존재해야 합니다.");

        // Post
        Optional<Post> optionalPost = postRepository.findById(Long.valueOf(request.getPostId()));
        if (optionalPost.isEmpty()) {
            throw new PostException("Post 가 존재하지 않습니다.");
        }
        Post post = optionalPost.get();

        // Comment
        Comment comment =
                Comment.builder()
                        .content(request.getContent())
                        .createdBy(member.getMemberId())
                        .build();
        comment.setPost(post);
        comment.setMember(member);
        commentRepository.save(comment);

        // 게시물 작성자
        Member postMember = post.getMember();

        // 게시물 작성자와 댓글 작성자가 다른 경우만 포인트 증감 처리
        if (!member.getMemberId().equals(postMember.getMemberId())) {

            // 댓글 작성자 point + 2
            MemberPoint memberPointFromMember = member.getMemberPoint();
            memberPointFromMember.setScore(memberPointFromMember.getScore() + PointType.CREATE_COMMENT.getScore());
            memberPointFromMember.setUpdatedBy(member.getMemberId());

            // 게시물 작성자 point + 1
            MemberPoint memberPointFromPost = postMember.getMemberPoint();
            memberPointFromPost.setScore(memberPointFromPost.getScore() + PointType.CREATE_BY.getScore());
            memberPointFromPost.setUpdatedBy(member.getMemberId());

            PointHistory pointHistoryForComment =
                    PointHistory.builder()
                            .memberId(member.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.CREATE.name())
                            .score(PointType.CREATE_COMMENT.getScore())
                            .createdBy(member.getMemberId())
                            .build();
            PointHistory pointHistoryForPost =
                    PointHistory.builder()
                            .memberId(postMember.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.CREATE_BY.name())
                            .score(PointType.CREATE_BY.getScore())
                            .createdBy(member.getMemberId())
                            .build();

            List<PointHistory> bunchOfPointHistory = new ArrayList<>();
            bunchOfPointHistory.add(pointHistoryForComment);
            bunchOfPointHistory.add(pointHistoryForPost);

            pointHistoryRepository.saveAll(bunchOfPointHistory);
        }

        return CommentDto.builder()
                .postId(post.getPostId())
                .commentId(comment.getCommentId())
                .build();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        // Comment
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()) {
            throw new CommentException("Comment 가 존재하지 않습니다.");
        }
        Comment comment = optionalComment.get();
        // Member
        Member member = comment.getMember();
        // Post
        Post post = comment.getPost();

        // 로그인 사용자가 댓글 작성자가 아니면 수정 불가
        if (!authorizationUtil.getLoginEmail().equals(member.getEmail())) {
            throw new CommentException("Comment 에 대한 권한이 없습니다.");
        }

        // 게시물 작성자
        Member postMember = post.getMember();

        // 게시물 작성자와 댓글 작성자가 다른 경우만 포인트 증감 처리
        if (!member.getMemberId().equals(postMember.getMemberId())) {

            // 댓글 작성자 point - 2
            MemberPoint memberPointFromMember = member.getMemberPoint();
            memberPointFromMember.setScore(memberPointFromMember.getScore() + PointType.DELETE_COMMENT.getScore());
            memberPointFromMember.setUpdatedBy(member.getMemberId());

            // 게시물 작성자 point - 1
            MemberPoint memberPointFromPost = postMember.getMemberPoint();
            memberPointFromPost.setScore(memberPointFromPost.getScore() + PointType.DELETE_BY.getScore());
            memberPointFromPost.setUpdatedBy(member.getMemberId());

            PointHistory pointHistoryForComment =
                    PointHistory.builder()
                            .memberId(member.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.DELETE.name())
                            .score(PointType.DELETE_COMMENT.getScore())
                            .createdBy(member.getMemberId())
                            .build();

            PointHistory pointHistoryForPost =
                    PointHistory.builder()
                            .memberId(postMember.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.DELETE_BY.name())
                            .score(PointType.DELETE_BY.getScore())
                            .createdBy(member.getMemberId())
                            .build();

            List<PointHistory> bunchOfPointHistory = new ArrayList<>();
            bunchOfPointHistory.add(pointHistoryForComment);
            bunchOfPointHistory.add(pointHistoryForPost);

            pointHistoryRepository.saveAll(bunchOfPointHistory);
        }

        commentRepository.delete(comment);
    }
}
