package com.colorpl.comment.domain;

import com.colorpl.member.Member;
import com.colorpl.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "CONTENT")
    private String comment_content;

    public void updateComment(Review review, Member member, String comment_content) {
        this.member = member;
        this.review = review;
        this.comment_content = comment_content;
    }

}
