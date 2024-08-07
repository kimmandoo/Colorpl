package com.colorpl.schedule.domain;

import com.colorpl.member.Member;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.review.domain.Review;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("R")
@Entity
public class ReservationSchedule extends Schedule {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVE_DETAIL_ID")
    private ReservationDetail reservationDetail;

    @Builder
    public ReservationSchedule(
        Long id,
        Member member,
        String image,
        Review review,
        ReservationDetail reservationDetail
    ) {
        super(id, member, image, review);
        this.reservationDetail = reservationDetail;

        member.getSchedules().add(this);
    }
}
