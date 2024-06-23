package com.board.api.domain.point.entity;

import com.board.api.domain.member.entity.Member;
import com.board.api.global.entity.DateAndAuthor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Point extends DateAndAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long pointId;

    @Column(nullable = false)
    private Long total;

    @Setter
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Point(Long total) {
        this.total = total;
    }
}
