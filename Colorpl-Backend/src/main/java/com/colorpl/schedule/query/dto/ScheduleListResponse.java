package com.colorpl.schedule.query.dto;

import com.colorpl.schedule.command.domain.CustomSchedule;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import com.colorpl.schedule.command.domain.Schedule;
import com.colorpl.show.domain.detail.Category;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Builder
@Getter
public class ScheduleListResponse {

    private Long id;
    private String imgUrl;
    private String seat;
    private LocalDateTime dateTime;
    private String name;
    private Category category;
    private String location;
    private Double latitude;
    private Double longitude;

    public static ScheduleListResponse from(Schedule schedule) {

        ScheduleListResponse response = null;

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/images/{image}")
            .buildAndExpand(schedule.getImage())
            .toUri();

        if (schedule instanceof CustomSchedule customSchedule) {
            response = ScheduleListResponse.builder()
                .id(customSchedule.getId())
                .imgUrl(uri.toString())
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
                .id(reservationSchedule.getId())
                .imgUrl(reservationSchedule.getImage())
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
