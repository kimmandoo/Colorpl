package com.colorpl.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateReservationScheduleRequest {

    private Long reservationDetailId;
}
