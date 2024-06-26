package com.board.api.domain.post.service;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.member.entity.MemberPoint;
import com.board.api.domain.member.enums.Action;
import com.board.api.domain.member.enums.Category;
import com.board.api.domain.member.repository.MemberRepository;
import com.board.api.domain.point.entity.PointHistory;
import com.board.api.domain.point.enums.PointType;
import com.board.api.domain.point.repository.PointHistoryRepository;
import com.board.api.domain.post.dto.PostCreationDto;
import com.board.api.domain.post.dto.request.PostRequest;
import com.board.api.domain.post.entity.Post;
import com.board.api.domain.post.repository.PostRepository;
import com.board.api.global.util.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PostCreationDto createPost(PostRequest postRequest) {
        // post 저장
        Member member = memberRepository.findByEmail(AuthorizationUtil.getLoginEmail());
        Post post =
                Post.builder()
                        .title(postRequest.getTitle())
                        .content(postRequest.getContent())
                        .createdBy(member.getMemberId())
                        .build();
        post.setMember(member);
        postRepository.save(post);

        // point +3
        MemberPoint memberPoint = member.getMemberPoint();
        memberPoint.setScore(memberPoint.getScore() + PointType.CREATE_POST.getScore());

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
}
