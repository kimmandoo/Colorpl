package com.colorpl.review.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class EmpathyId implements Serializable {
    private Long reviewId;
    private Integer memberId;

    // Constructors
    public EmpathyId() {}

    public EmpathyId(Long reviewId, Integer memberId) {
        this.reviewId = reviewId;
        this.memberId = memberId;
    }

    // Getters and setters
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    // Equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpathyId empathyId = (EmpathyId) o;
        return Objects.equals(reviewId, empathyId.reviewId) &&
                Objects.equals(memberId, empathyId.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, memberId);
    }
}
