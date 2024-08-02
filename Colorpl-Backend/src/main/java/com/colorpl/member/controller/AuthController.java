package com.colorpl.member.controller;

import com.colorpl.global.config.GoogleTokenVerifier;
import com.colorpl.member.dto.SignInResponse;
import com.colorpl.member.dto.TokenVerificationRequest;
import com.colorpl.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class AuthController {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final MemberService memberService;

    @Autowired
    public AuthController(GoogleTokenVerifier googleTokenVerifier, MemberService memberService) {
        this.googleTokenVerifier = googleTokenVerifier;
        this.memberService = memberService;
    }

    @PostMapping("/verify-token")
    public String verifyToken(@RequestBody TokenVerificationRequest request) {
        try {
            // ID 토큰 검증 및 이메일 추출
            String email = googleTokenVerifier.verifyToken(request.getIdToken(), request.getClientId());
            return "Email: " + email;
        } catch (IOException | GeneralSecurityException e) {
            // 예외 발생 시 에러 메시지 반환
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/oauth-sign-in")
    @Operation(summary = "구글 로그인", description = "Email을 사용한 구글 로그인, 사용자가 없으면 가입까지 진행")
    public ResponseEntity<SignInResponse> oauthSignIn(@RequestBody TokenVerificationRequest request) {
        try {
            // ID 토큰 검증 및 이메일 추출
            String email = googleTokenVerifier.verifyToken(request.getIdToken(), request.getClientId());
            // 회원 가입 또는 로그인 처리
            SignInResponse response = memberService.oauthSignIn(email);
            return ResponseEntity.ok(response);
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
