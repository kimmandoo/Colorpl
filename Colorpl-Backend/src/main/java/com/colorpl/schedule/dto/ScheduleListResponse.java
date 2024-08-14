package com.colorpl.schedule.dto;

import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.domain.Schedule;
import com.colorpl.show.domain.Category;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
//        } else if (schedule instanceof ReservationSchedule reservationSchedule) {
//            response = builder
//                .seat(reservationSchedule.getReservation().getReservationDetails().get(0).getRow() + " "
//                    + reservationSchedule.getReservation().getReservationDetails().get(0).getCol())
//                .dateTime(
//                    reservationSchedule.getReservation().getReservationDetails().get(0).getShowSchedule().getDateTime())
//                .name(reservationSchedule.getReservation().getReservationDetails().get(0).getShowSchedule().getShowDetail()
//                    .getName())
//                .category(
//                    reservationSchedule.getReservation().getReservationDetails().get(0).getShowSchedule().getShowDetail()
//                        .getCategory())
//                .build();
//        }
        } else if (schedule instanceof ReservationSchedule reservationSchedule) {

            List<String> seats = reservationSchedule.getReservation().getReservationDetails().stream()
                .map(detail -> convertToSeatFormat(detail.getRow(), detail.getCol()))
                .collect(Collectors.toList());

            // 좌석 정보를 문자열로 변환
            String seatInfo = String.join(", ", seats);

            response = builder
                .seat(seatInfo) // 모든 좌석 정보를 문자열로 설정
                .dateTime(
                    reservationSchedule.getReservation().getReservationDetails().get(0).getShowSchedule().getDateTime())
                .name(reservationSchedule.getReservation().getReservationDetails().get(0).getShowSchedule().getShowDetail()
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

    // 좌석 변환 메서드
    private static String convertToSeatFormat(int row, int col) {
        // row를 알파벳으로 변환 (0 -> A, 1 -> B, ...)
        char rowChar = (char) ('A' + row);
        // col을 숫자로 변환 (1부터 시작)
        return rowChar + String.valueOf(col + 1);
    }
}
