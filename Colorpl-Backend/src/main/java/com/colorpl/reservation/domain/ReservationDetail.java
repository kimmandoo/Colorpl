package com.colorpl.reservation.domain;

import com.colorpl.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ReservationDetail extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "RESERVE_DETAIL_ID")
    private Long id;

    @Column(name = "SEAT_ROW")
    private Byte row;

    @Column(name = "SEAT_COL")
    private Byte col;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVE_ID", nullable = false)
    private Reservation reservation;

    //공연일시Id




}