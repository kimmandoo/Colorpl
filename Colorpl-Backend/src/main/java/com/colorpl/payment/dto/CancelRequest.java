package com.colorpl.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class CancelRequest {
    private String receiptId;
    private String cancelUsername;
    private String cancelMessage;
    private double amount;  // 추가된 필드

    // Getters and setters
}


