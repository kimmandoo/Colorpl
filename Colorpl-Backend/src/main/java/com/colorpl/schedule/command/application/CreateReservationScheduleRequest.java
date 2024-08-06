package com.colorpl.schedule.command.application;

import lombok.Getter;

@Getter
public class CreateReservationScheduleRequest {

    private Integer memberId;
    private Long reservationDetailId;
}
