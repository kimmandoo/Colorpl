package com.colorpl.reservation.dto;

import com.colorpl.reservation.domain.Reservation;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private Integer memberId;
    private LocalDateTime date;
    private String amount;
    private String comment;
    private boolean isRefunded;
    private List<ReservationDetailDTO> reservationDetails;

    //--DTO로 변환--
    public static ReservationDTO toReservationDTO(Reservation reservation) {
        return ReservationDTO.builder()
            .id(reservation.getId())
            .memberId(reservation.getMember().getId())
            .date(reservation.getDate())
            .amount(reservation.getAmount())
            .comment(reservation.getComment())
            .isRefunded(reservation.isRefunded())
            .reservationDetails(reservation.getReservationDetails()
                .stream()
                .map(ReservationDetailDTO::toReservationDetailDTO)
                .collect(Collectors.toList()))
            .build();
    }
}