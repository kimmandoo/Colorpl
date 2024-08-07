package com.colorpl.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentItem {
    private String itemId;
    private String itemName;
    private int quantity;
    private double price;

    // Getters and setters
}