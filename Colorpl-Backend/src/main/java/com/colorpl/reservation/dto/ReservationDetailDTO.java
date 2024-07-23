package com.colorpl.reservation.dto;

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
    private Integer showScheduleId;
}