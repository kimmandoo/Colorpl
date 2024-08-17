package com.colorpl.schedule.dto;

import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.domain.Schedule;
import com.colorpl.schedule.domain.ScheduleType;
import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.ShowDetail;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetScheduleResponse {

    private ScheduleType type;
    private String image;
    private String seat;
    private LocalDateTime dateTime;
    private String name;
    private Category category;
    private String location;
    private Double latitude;
    private Double longitude;
    private Boolean reviewExists;
    private Long reviewId;

    public static GetScheduleResponse from(Schedule schedule) {

        GetScheduleResponseBuilder builder = GetScheduleResponse.builder();

        if (schedule.getReview().isPresent()) {
            builder.reviewExists(true);
            builder.reviewId(schedule.getReview().get().getId());
        } else {
            builder.reviewExists(false);
        }

        if (schedule instanceof CustomSchedule customSchedule) {
            return from(customSchedule, builder);
        }
        if (schedule instanceof ReservationSchedule reservationSchedule) {
            return from(reservationSchedule, builder);
        }
        throw new IllegalArgumentException();
    }

    private static GetScheduleResponse from(CustomSchedule customSchedule,
        GetScheduleResponseBuilder builder) {
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/images/{image}")
            .buildAndExpand(customSchedule.getImage())
            .toUri();
        return builder
            .type(ScheduleType.CUSTOM)
            .image(uri.toString())
            .seat(customSchedule.getSeat())
            .dateTime(customSchedule.getDateTime())
            .name(customSchedule.getName())
            .category(customSchedule.getCategory())
            .location(customSchedule.getLocation())
            .latitude(customSchedule.getLatitude())
            .longitude(customSchedule.getLongitude())
            .build();
    }

    private static GetScheduleResponse from(ReservationSchedule reservationSchedule,
        GetScheduleResponseBuilder builder) {
        ShowDetail showDetail = reservationSchedule.getReservation().getReservationDetails().get(0)
            .getShowSchedule().getShowDetail();


        List<String> seats = reservationSchedule.getReservation().getReservationDetails()
                .stream()
                .map(detail -> convertToSeatFormat(detail.getRow(), detail.getCol()))
                .collect(Collectors.toList());

        // 좌석 정보를 문자열로 변환
        String seatInfo = String.join(", ", seats);

        return builder
            .type(ScheduleType.RESERVATION)
            .image(showDetail.getPosterImagePath())
            .seat(seatInfo)
            .dateTime(reservationSchedule.getReservation().getReservationDetails().get(0)
                .getShowSchedule().getDateTime())
            .name(showDetail.getName())
            .category(showDetail.getCategory())
            .location(String.join(" ", showDetail.getHall().getTheater().getName(),
                showDetail.getHall().getName()))
            .latitude(showDetail.getHall().getTheater().getLatitude())
            .longitude(showDetail.getHall().getTheater().getLongitude())
            .build();
    }


    // 좌석 변환 메서드
    private static String convertToSeatFormat(int row, int col) {
        // row를 알파벳으로 변환 (0 -> A, 1 -> B, ...)
        char rowChar = (char) ('A' + row);
        // col을 숫자로 변환 (1부터 시작)
        return rowChar + String.valueOf(col + 1);
    }
}
