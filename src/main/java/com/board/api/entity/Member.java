package com.board.api.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class Member extends DateAndAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String idType;

    @Column(nullable = false)
    private String idValue;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberRole memberRole;

    public void setMemberRole(MemberRole memberRole) {
        memberRole.setMember(this);
        this.memberRole = memberRole;
    }

    @Builder
    public Member(String email, String password, String name, String idType, String idValue) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.idType = idType;
        this.idValue = idValue;
    }

}
