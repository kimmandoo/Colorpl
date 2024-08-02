package com.colorpl.member.dto;

import lombok.Getter;

@Getter
public class TokenVerificationRequest {
    // Getters and setters
    private String idToken;
    private String clientId;

    public void updateIdToken(String idToken) {
        this.idToken = idToken;
    }
    public void updateClientId(String clientId) {
        this.clientId = clientId;
    }
}
