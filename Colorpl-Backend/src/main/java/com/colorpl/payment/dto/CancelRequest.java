package com.colorpl.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelRequest {
    private String receiptId;
    private String cancelUsername;
    private String cancelMessage;

    // Getters and setters
}


