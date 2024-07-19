package com.colorpl.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDetail {

    @Id
    @GeneratedValue
    @Column(name = "RESERVE_ID")
    private Long id;

    @Column(name = "SEAT_ROW")
    private Byte row;

    @Column(name = "SEAT_COL")
    private Byte col;




}
