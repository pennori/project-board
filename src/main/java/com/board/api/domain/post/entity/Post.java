package com.board.api.domain.post.entity;

import com.board.api.domain.comment.entity.Comment;
import com.board.api.domain.member.entity.Member;
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
public class Post extends DateAndAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Comment> bunchOfComment;

    public void setMember(Member member) {
        member.addPost(this);
        this.member = member;
    }

    @Builder
    public Post(String title, String content, Long createdBy) {
        super(createdBy);
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        if (ObjectUtils.isEmpty(bunchOfComment)) {
            bunchOfComment = new ArrayList<>();
        }
        bunchOfComment.add(comment);
    }
}
