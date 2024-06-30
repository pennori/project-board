package com.board.api.domain.post.service;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.enums.Action;
import com.board.api.domain.member.enums.Category;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.entity.Point;
import com.board.api.domain.point.enums.PointEvent;
import com.board.api.domain.point.repository.PointRepository;
import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.request.PostCreateRequest;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostCreateService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PointRepository pointRepository;
    private final AuthorizationUtil authorizationUtil;

    @Transactional
    public PostCreationDto createPost(PostCreateRequest request) {
        Assert.notNull(request, "호출시 요청 정보가 비어서 들어올 수 없습니다.");
        // Member
        Member member = memberRepository.findByEmail(authorizationUtil.getLoginEmail());
        Assert.notNull(member, "로그인한 회원의 요청이므로 회원정보가 존재해야 합니다.");
        // post 저장
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
        memberPoint.setScore(memberPoint.getScore() + PointEvent.CREATE_POST.getScore());
        memberPoint.setUpdatedBy(member.getMemberId());

        // point history 저장
        Point point =
                Point.builder()
                        .postId(post.getPostId())
                        .memberId(member.getMemberId())
                        .action(Action.CREATE.name())
                        .category(Category.POST.name())
                        .score(PointEvent.CREATE_POST.getScore())
                        .createdBy(member.getMemberId())
                        .build();
        pointRepository.save(point);

        return PostCreationDto.builder().postId(post.getPostId()).build();
    }
}
