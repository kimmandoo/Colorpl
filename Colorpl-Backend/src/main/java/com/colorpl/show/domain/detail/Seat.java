package com.colorpl.show.domain.detail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Column(name = "SEAT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "SHOW_DETAIL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ShowDetail showDetail;

    @Column(name = "SEAT_ROW")
    private Integer row;

    @Column(name = "SEAT_COL")
    private Integer col;

    @Column(name = "SEAT_CLASS")
    private String seatClass;
}
