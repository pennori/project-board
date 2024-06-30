package com.board.api.domain.comment.service;

import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.exception.CommentException;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.enums.Action;
import com.board.api.domain.member.enums.Category;
import com.board.api.domain.point.entity.Point;
import com.board.api.domain.point.enums.PointEvent;
import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.entity.Post;
import com.board.api.global.util.AuthorizationUtil;
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
public class CommentDeleteService {
    private final CommentRepository commentRepository;
    private final PointRepository pointRepository;
    private final AuthorizationUtil authorizationUtil;

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
            memberPointFromMember.setScore(memberPointFromMember.getScore() + PointEvent.DELETE_COMMENT.getScore());
            memberPointFromMember.setUpdatedBy(member.getMemberId());

            // 게시물 작성자 point - 1
            MemberPoint memberPointFromPost = postMember.getMemberPoint();
            memberPointFromPost.setScore(memberPointFromPost.getScore() + PointEvent.DELETE_BY.getScore());
            memberPointFromPost.setUpdatedBy(member.getMemberId());

            Point pointForComment =
                    Point.builder()
                            .memberId(member.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.DELETE.name())
                            .score(PointEvent.DELETE_COMMENT.getScore())
                            .createdBy(member.getMemberId())
                            .build();

            Point pointForPost =
                    Point.builder()
                            .memberId(postMember.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.DELETE_BY.name())
                            .score(PointEvent.DELETE_BY.getScore())
                            .createdBy(member.getMemberId())
                            .build();

            List<Point> bunchOfPoint = new ArrayList<>();
            bunchOfPoint.add(pointForComment);
            bunchOfPoint.add(pointForPost);

            pointRepository.saveAll(bunchOfPoint);
        }

        commentRepository.delete(comment);
    }
}
