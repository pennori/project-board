package com.board.api.domain.member.entity;

import com.board.api.domain.point.entity.Point;
import com.board.api.domain.post.entity.Post;
import com.board.api.global.entity.DateAndAuthor;
import io.jsonwebtoken.lang.Objects;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    private String regNo;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberRole memberRole;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Point point;


    @OneToMany(mappedBy = "member")
    private List<Post> bunchOfPost;

    public void setMemberRole(MemberRole memberRole) {
        memberRole.setMember(this);
        this.memberRole = memberRole;
    }

    public void setPoint(Point point) {
        point.setMember(this);
        this.point = point;
    }

    public void addPost(Post post) {
        if (Objects.isEmpty(bunchOfPost)) {
            bunchOfPost = new ArrayList<>();
        }
        bunchOfPost.add(post);
    }

    @Builder
    public Member(String email, String password, String name, String regNo) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }

}
