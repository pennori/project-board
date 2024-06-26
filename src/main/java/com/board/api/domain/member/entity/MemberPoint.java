package com.board.api.domain.member.entity;

import com.board.api.global.entity.DateAndAuthor;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class MemberPoint extends DateAndAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long memberPointId;

    @Setter
    @Column(nullable = false)
    private Long score;

    @Setter
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public MemberPoint(Long score, Long createdBy) {
        super(createdBy);
        this.score = score;
    }
}
