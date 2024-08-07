package com.colorpl.schedule.command.domain;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class Schedule {

    @Column(name = "SCHEDULE_ID")
    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
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
