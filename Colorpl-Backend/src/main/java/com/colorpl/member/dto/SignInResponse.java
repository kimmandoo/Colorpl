package com.colorpl.member.dto;

import com.colorpl.member.MemberType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
        private String email;
        private MemberType type;
        private String accessToken;	// token -> accessToken으로 액세스 토큰임을 명시
        private String refreshToken;
}