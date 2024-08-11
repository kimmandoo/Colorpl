package com.colorpl.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    private String receiptId;
    private String orderName;
    private String purchasedAt;
    private int price;
    private String statusLocale;
    private List<SeatInfoDTO> seats;
    private LocalDateTime showDateTime;
    private String theaterName;
    private String showDetailPosterImagePath;
}
