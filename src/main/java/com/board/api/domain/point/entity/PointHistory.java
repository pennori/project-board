package com.board.api.domain.point.entity;

import com.board.api.global.entity.DateAndAuthor;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PointHistory extends DateAndAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long pointHistoryId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Long score;

    @Builder
    public PointHistory(Long memberId, Long postId, Long commentId, String category, String action, Long score, Long createdBy) {
        super(createdBy);
        this.memberId = memberId;
        this.postId = postId;
        this.commentId = commentId;
        this.category = category;
        this.action = action;
        this.score = score;
    }
}
