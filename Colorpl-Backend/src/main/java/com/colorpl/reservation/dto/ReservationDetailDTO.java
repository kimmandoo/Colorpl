package com.colorpl.reservation.dto;

import com.colorpl.reservation.domain.ReservationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailDTO {

    private Long id;
    private Byte row;
    private Byte col;
    private Long showScheduleId;

    public static ReservationDetailDTO toReservationDetailDTO(ReservationDetail reservationDetail) {
        return ReservationDetailDTO.builder()
            .id(reservationDetail.getId())
            .row(reservationDetail.getRow())
            .col(reservationDetail.getCol())
            .showScheduleId(reservationDetail.getShowSchedule().getId())
            .build();
    }
}