package com.colorpl.schedule.dto;

import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.domain.Schedule;
import com.colorpl.show.domain.Category;
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
    private Boolean reviewExists;
    private Long reviewId;

    public static ScheduleListResponse from(Schedule schedule) {

        ScheduleListResponse response = null;

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/images/{image}")
            .buildAndExpand(schedule.getImage())
            .toUri();

        ScheduleListResponseBuilder builder = ScheduleListResponse.builder()
            .id(schedule.getId())
            .imgUrl(uri.toString());

        if (schedule.getReview().isPresent()) {
            builder.reviewExists(true);
            builder.reviewId(schedule.getReview().get().getId());
        } else {
            builder.reviewExists(false);
        }

        if (schedule instanceof CustomSchedule customSchedule) {
            response = builder
                .seat(customSchedule.getSeat())
                .dateTime(customSchedule.getDateTime())
                .name(customSchedule.getName())
                .category(customSchedule.getCategory())
                .location(customSchedule.getLocation())
                .latitude(customSchedule.getLatitude())
                .longitude(customSchedule.getLongitude())
                .build();
        } else if (schedule instanceof ReservationSchedule reservationSchedule) {
            response = builder
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
