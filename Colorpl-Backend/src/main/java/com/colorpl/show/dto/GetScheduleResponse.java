package com.colorpl.show.dto;

import com.colorpl.show.domain.SeatClass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetScheduleResponse {

    private Integer row;
    private Integer col;
    private String name;
    private SeatClass grade;
    private Boolean isReserved;
}
