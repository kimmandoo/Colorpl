package com.colorpl.show.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatListResponse {

    private Long id;
    private Integer row;
    private Integer col;
    private String seatClass;
}
