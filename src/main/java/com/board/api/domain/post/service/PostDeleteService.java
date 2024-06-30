package com.board.api.domain.post.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostDeleteService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PointRepository pointRepository;
    private final CommentRepository commentRepository;
    private final AuthorizationUtil authorizationUtil;
    private final MessageSource messageSource;

    @CacheEvict(value = "board", key = "#postId")
    @Transactional
    public void deletePost(Long postId) {
        // Post
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new PostException(messageSource.getMessage("exception.notfound", new String[]{"Post"}, Locale.getDefault()));
        }
        Post post = optionalPost.get();

        // 로그인 사용자가 글 작성자가 아니면 삭제 불가
        if (!authorizationUtil.getLoginEmail().equals(post.getMember().getEmail())) {
            throw new PostException(messageSource.getMessage("exception.unauthorized", new String[]{"Post"}, Locale.getDefault()));
        }

        // 삭제시 차감할 포인트 집합 설정
        Map<String, Long> decreaseCase = new HashMap<>();

        // 게시물 작성 포인트 3점
        Long postMemberId = post.getMember().getMemberId();
        decreaseCase.put("post_member_" + postMemberId, decreaseCase.getOrDefault("post_member_" + postMemberId, 0L) + PointEvent.CREATE_POST.getScore());

        // PointHistory param
        List<Point> bunchOfPoint = new ArrayList<>();

        // comment 삭제
        List<Comment> bunchOfComment = post.getBunchOfComment();
        if (!ObjectUtils.isEmpty(bunchOfComment)) {

            for (Comment comment : bunchOfComment) {
                Long decrease = 0L;
                Long commentMemberId = comment.getMember().getMemberId();
                // 게시물 작성자와 댓글 작성자가 다른 경우만 포인트 증감 처리
                if (!Objects.equals(commentMemberId, postMemberId)) {
                    decreaseCase.put("post_member_" + postMemberId, decreaseCase.getOrDefault("post_member_" + postMemberId, 0L) + PointEvent.CREATE_BY.getScore());
                    decreaseCase.put("comment_member_" + commentMemberId, decreaseCase.getOrDefault("comment_member_" + commentMemberId, 0L) + PointEvent.CREATE_COMMENT.getScore());

                    decrease = PointEvent.DELETE_COMMENT.getScore();
                }

                // comment 삭제에 대한 point history 집합
                Point point =
                        Point.builder()
                                .memberId(commentMemberId)
                                .postId(postId)
                                .commentId(comment.getCommentId())
                                .category(Category.COMMENT.name())
                                .action(Action.DELETE.name())
                                .score(decrease)
                                .createdBy(postMemberId)
                                .build();
                bunchOfPoint.add(point);

            }

            commentRepository.deleteAll(bunchOfComment);
        }

        // post 삭제에 대한 point history 저장
        Point point =
                Point.builder()
                        .memberId(postMemberId)
                        .postId(postId)
                        .commentId(0L)
                        .category(Category.POST.name())
                        .action(Action.DELETE.name())
                        .score(PointEvent.DELETE_POST.getScore())
                        .createdBy(postMemberId)
                        .build();
        bunchOfPoint.add(point);

        // PointHistory 저장
        pointRepository.saveAll(bunchOfPoint);

        // post 삭제
        postRepository.delete(post);

        // 차감할 포인트가 있는 사용자에 대한 포인트 차감 및 이력 처리
        for (Map.Entry<String, Long> entry : decreaseCase.entrySet()) {
            String key = entry.getKey();
            Long memberIdForDelete = Long.valueOf(key.split("_")[2]);
            Long scoreForDelete = entry.getValue();

            Optional<Member> optionalMember = memberRepository.findById(memberIdForDelete);
            if (optionalMember.isPresent()) {
                // MemberPoint 조정
                MemberPoint memberPoint = optionalMember.get().getMemberPoint();
                memberPoint.setScore(memberPoint.getScore() - scoreForDelete);
            }

        }

    }

}
