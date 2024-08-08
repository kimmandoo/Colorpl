package com.colorpl.schedule.domain;

import com.colorpl.member.Member;
import com.colorpl.review.domain.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Schedule {

    @Id
    @GeneratedValue
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "SCHEDULE_IMAGE")
    private String image;

    @OneToOne(mappedBy = "schedule")
    private Review review;

    public Optional<Review> getReview() {
        return Optional.ofNullable(review);
    }

    public void updateMember(Member member) {
        if (this.member != null) {
            this.member.getSchedules().remove(this);
        }
        this.member = member;
        member.getSchedules().add(this);
    }
}
