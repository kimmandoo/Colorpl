package com.colorpl.show.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Seat {

    @ToString.Exclude
    @Column(name = "SEAT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ToString.Exclude
    @JoinColumn(name = "SHOW_DETAIL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ShowDetail showDetail;

    @Column(name = "SEAT_ROW")
    private Integer row;

    @Column(name = "SEAT_COL")
    private Integer col;

    @ToString.Exclude
    @Column(name = "SEAT_CLASS")
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
}