package com.colorpl.schedule.command.domain;

import com.colorpl.reservation.domain.ReservationDetail;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@DiscriminatorValue("RESERVATION")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ReservationSchedule extends Schedule {

    @JoinColumn(name = "RESERVE_DETAIL_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private ReservationDetail reservationDetail;
}
