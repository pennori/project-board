package com.board.api.domain.comment.service;

import com.board.api.domain.comment.dto.CommentDto;
import com.board.api.domain.comment.dto.request.CommentRequest;
import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.comment.repository.CommentRepository;
import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.enums.Action;
import com.board.api.domain.member.enums.Category;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.entity.Point;
import com.board.api.domain.point.enums.PointEvent;
import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.exception.PostException;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentCreateService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PointRepository pointRepository;
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
            memberPointFromMember.setScore(memberPointFromMember.getScore() + PointEvent.CREATE_COMMENT.getScore());
            memberPointFromMember.setUpdatedBy(member.getMemberId());

            // 게시물 작성자 point + 1
            MemberPoint memberPointFromPost = postMember.getMemberPoint();
            memberPointFromPost.setScore(memberPointFromPost.getScore() + PointEvent.CREATE_BY.getScore());
            memberPointFromPost.setUpdatedBy(member.getMemberId());

            Point pointForComment =
                    Point.builder()
                            .memberId(member.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.CREATE.name())
                            .score(PointEvent.CREATE_COMMENT.getScore())
                            .createdBy(member.getMemberId())
                            .build();
            Point pointForPost =
                    Point.builder()
                            .memberId(postMember.getMemberId())
                            .postId(post.getPostId())
                            .commentId(comment.getCommentId())
                            .category(Category.COMMENT.name())
                            .action(Action.CREATE_BY.name())
                            .score(PointEvent.CREATE_BY.getScore())
                            .createdBy(member.getMemberId())
                            .build();

            List<Point> bunchOfPoint = new ArrayList<>();
            bunchOfPoint.add(pointForComment);
            bunchOfPoint.add(pointForPost);

            pointRepository.saveAll(bunchOfPoint);
        }

        return CommentDto.builder()
                .postId(post.getPostId())
                .commentId(comment.getCommentId())
                .build();
    }

}
