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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_DETAIL_ID")
    private ShowDetail showDetail;

    @Column(name = "SEAT_ROW")
    private Integer row;

    @Column(name = "SEAT_COL")
    private Integer col;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEAT_CLASS")
    private SeatClass seatClass;

    @Builder
    public Seat(Integer col, Long id, Integer row, SeatClass seatClass, ShowDetail showDetail) {
        this.col = col;
        this.id = id;
        this.row = row;
        this.seatClass = seatClass;
        this.showDetail = showDetail;
        showDetail.getSeats().add(this);
    }
}
