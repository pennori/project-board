package com.board.api.domain.member.entity;

import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.post.entity.Post;
import com.board.api.global.entity.DateAndAuthor;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
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
    private MemberPoint memberPoint;


    @OneToMany(mappedBy = "member")
    private List<Post> bunchOfPost;

    @OneToMany(mappedBy = "member")
    private List<Comment> bunchOfComment;

    public void setMemberRole(MemberRole memberRole) {
        memberRole.setMember(this);
        this.memberRole = memberRole;
    }

    public void setMemberPoint(MemberPoint memberPoint) {
        memberPoint.setMember(this);
        this.memberPoint = memberPoint;
    }

    public void addPost(Post post) {
        if (ObjectUtils.isEmpty(bunchOfPost)) {
            bunchOfPost = new ArrayList<>();
        }
        bunchOfPost.add(post);
    }

    @Builder
    public Member(String email, String password, String name, String regNo, Long createdBy) {
        super(createdBy);
        this.email = email;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }



    public void addComment(Comment comment) {
        if (ObjectUtils.isEmpty(bunchOfComment)) {
            bunchOfComment = new ArrayList<>();
        }
        bunchOfComment.add(comment);
    }
}
