package com.board.api.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class MemberRole {

    @Builder
    public MemberRole(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long memberRoleId;

    @Column(nullable = false)
    private String name;

    @Setter
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
