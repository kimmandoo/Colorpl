package com.colorpl.schedule.query.dto;

import com.colorpl.schedule.command.domain.CustomSchedule;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import com.colorpl.schedule.command.domain.Schedule;
import com.colorpl.show.domain.detail.Category;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class ScheduleListResponse {

    private String seat;
    private LocalDateTime dateTime;
    private String name;
    private Category category;
    private String location;
    private Double latitude;
    private Double longitude;

    public static ScheduleListResponse from(Schedule schedule) {

        ScheduleListResponse response = null;

        if (schedule instanceof CustomSchedule customSchedule) {
            response = ScheduleListResponse.builder()
                .seat(customSchedule.getSeat())
                .dateTime(customSchedule.getDateTime())
                .name(customSchedule.getName())
                .category(customSchedule.getCategory())
                .location(customSchedule.getLocation())
                .latitude(customSchedule.getLatitude())
                .longitude(customSchedule.getLongitude())
                .build();
        } else if (schedule instanceof ReservationSchedule reservationSchedule) {
            response = ScheduleListResponse.builder()
                .seat(reservationSchedule.getReservationDetail().getRow() + " "
                    + reservationSchedule.getReservationDetail().getCol())
                .dateTime(
                    reservationSchedule.getReservationDetail().getShowSchedule().getDateTime())
                .name(reservationSchedule.getReservationDetail().getShowSchedule().getShowDetail()
                    .getName())
                .category(
                    reservationSchedule.getReservationDetail().getShowSchedule().getShowDetail()
                        .getCategory())
                .build();
        }
        return response;
    }
}
