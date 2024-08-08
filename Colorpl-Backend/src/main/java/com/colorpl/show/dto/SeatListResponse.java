package com.colorpl.show.dto;

import com.colorpl.show.domain.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatListResponse {

    private Long id;
    private Integer row;
    private Integer col;
    private String seatClass;

    public static SeatListResponse from(Seat seat) {
        return SeatListResponse.builder()
            .id(seat.getId())
            .row(seat.getRow())
            .col(seat.getCol())
            .seatClass(seat.getSeatClass())
            .build();
    }
}
