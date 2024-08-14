package com.colorpl.schedule.dto;

import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.domain.Schedule;
import com.colorpl.show.domain.Category;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
@Builder
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

        ScheduleListResponseBuilder builder = ScheduleListResponse.builder()
            .id(schedule.getId());

        if (schedule.getReview().isPresent()) {
            builder.reviewExists(true);
            builder.reviewId(schedule.getReview().get().getId());
        } else {
            builder.reviewExists(false);
        }

        if (schedule instanceof CustomSchedule customSchedule) {
            response = builder
                .imgUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/{image}")
                    .buildAndExpand(customSchedule.getImage())
                    .toUri()
                    .toString())
                .seat(customSchedule.getSeat())
                .dateTime(customSchedule.getDateTime())
                .name(customSchedule.getName())
                .category(customSchedule.getCategory())
                .location(customSchedule.getLocation())
                .latitude(customSchedule.getLatitude())
                .longitude(customSchedule.getLongitude())
                .build();
        } else if (schedule instanceof ReservationSchedule reservationSchedule) {
            ReservationDetail reservationDetail =
                reservationSchedule
                    .getReservation()
                    .getReservationDetails()
                    .get(0);
            response = builder
                .imgUrl(reservationDetail.getShowSchedule().getShowDetail().getPosterImagePath())
                .seat(reservationDetail.getRow() + " " + reservationDetail.getCol())
                .dateTime(reservationDetail.getShowSchedule().getDateTime())
                .name(reservationDetail.getShowSchedule().getShowDetail().getName())
                .category(reservationDetail.getShowSchedule().getShowDetail().getCategory())
                .location(reservationDetail.getShowSchedule().getShowDetail().getHall().getTheater()
                    .getName())
                .latitude(reservationDetail.getShowSchedule().getShowDetail().getHall().getTheater()
                    .getLatitude())
                .longitude(
                    reservationDetail.getShowSchedule().getShowDetail().getHall().getTheater()
                        .getLongitude())
                .build();
        }

        return response;
    }
}
