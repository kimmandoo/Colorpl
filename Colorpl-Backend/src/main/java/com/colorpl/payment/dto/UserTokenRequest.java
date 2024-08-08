package com.colorpl.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTokenRequest {
    private String userId;
    private String email;
    private String phone;
    private int gender;
    private String birth;

    // Getters and setters
}