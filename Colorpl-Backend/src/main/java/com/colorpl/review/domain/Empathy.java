package com.colorpl.review.domain;

import com.colorpl.member.Member;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Builder;

@Builder
@Entity
public class Empathy {
    @EmbeddedId
    private EmpathyId id;

    @ManyToOne
    @MapsId("reviewId")
    private Review review;

    @ManyToOne
    @MapsId("memberId")
    private Member member;

    // Constructors
    public Empathy() {}

    public Empathy(EmpathyId id, Review review, Member member) {
        this.id = id;
        this.review = review;
        this.member = member;
    }

    // Getters and setters
    public EmpathyId getId() {
        return id;
    }

    public void setId(EmpathyId id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
