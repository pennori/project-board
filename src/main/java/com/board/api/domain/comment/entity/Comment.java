package com.board.api.domain.comment.entity;

import com.board.api.domain.member.entity.Member;
import com.board.api.domain.post.entity.Post;
import com.board.api.global.entity.DateAndAuthor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends DateAndAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void setMember(Member member) {
        member.addComment(this);
        this.member = member;
    }

    public void setPost(Post post) {
        post.addComment(this);
        this.post = post;
    }
}
