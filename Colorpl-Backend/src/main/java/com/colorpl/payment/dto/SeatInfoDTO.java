package com.colorpl.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatInfoDTO {
    private Byte row;
    private Byte col;
}
