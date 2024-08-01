package com.colorpl.global.config;

import com.colorpl.member.MemberRefreshToken;
import com.colorpl.member.repository.BlackListRepository;
import com.colorpl.member.repository.MemberRefreshTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TokenProvider {
    private final String secretKey;
    private final long expirationMinutes;	// hours -> minutes
    private final long refreshExpirationHours;
    private final String issuer;
    private final long reissueLimit;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration-minutes}") long expirationMinutes,
            @Value("${refresh-expiration-hours}") long refreshExpirationHours,	// 추가
            @Value("${jwt.issuer}") String issuer,
            MemberRefreshTokenRepository memberRefreshTokenRepository,
            BlackListRepository blackListRepository
    ) {
        this.secretKey = secretKey;
        this.expirationMinutes = expirationMinutes;
        this.refreshExpirationHours = refreshExpirationHours;	//추가
        this.issuer = issuer;
        this.memberRefreshTokenRepository = memberRefreshTokenRepository;	// 추가
        this.blackListRepository = blackListRepository;
        reissueLimit = refreshExpirationHours * 60 / expirationMinutes;
    }

    public String createAccessToken(String userSpecification) {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setSubject(userSpecification)  // JWT 토큰 제목
                .setIssuer(issuer)  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

//    @Transactional
//    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
//        String subject = decodeJwtPayloadSubject(oldAccessToken);
//        memberRefreshTokenRepository.findByMemberIdAndReissueCountLessThan(UUID.fromString(subject.split(":")[0]), reissueLimit)
//                .ifPresentOrElse(
//                        MemberRefreshToken::increaseReissueCount,
//                        () -> { throw new ExpiredJwtException(null, null, "Refresh token expired."); }
//                );
//        return createAccessToken(subject);
//    }
    @Transactional
    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        // JWT payload의 subject를 디코딩하여 memberId와 기타 정보 추출
        String subject = decodeJwtPayloadSubject(oldAccessToken);

        // memberId를 추출 (여기서는 Integer 타입으로 사용)
        Integer memberId = Integer.parseInt(subject.split(":")[0]);

        // reissueCount가 reissueLimit보다 작은 MemberRefreshToken을 찾음
        MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository
                .findByMemberIdAndReissueCountLessThan(memberId, reissueLimit)
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token expired."));

        // reissueCount 증가
        memberRefreshToken.increaseReissueCount();

        // 새로운 access token 생성
        return createAccessToken(subject);
    }


    public String createRefreshToken() {
        return Jwts.builder()
            .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
            .setIssuer(issuer)
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
            .setExpiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
            .compact();
    }

    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        // refreshToken의 유효성을 검사

        if (blackListRepository.existsByInvalidRefreshToken(refreshToken)) {
            throw new ExpiredJwtException(null, null, "Refresh token is blacklisted.");
        }
        validateAndParseToken(refreshToken);

        // oldAccessToken에서 memberId 추출
        String memberId = decodeJwtPayloadSubject(oldAccessToken).split(":")[0];

        // memberId와 reissueCount 조건을 만족하는 MemberRefreshToken 찾기
        memberRefreshTokenRepository.findByMemberIdAndReissueCountLessThan(Integer.parseInt(memberId),
                reissueLimit)
            .filter(memberRefreshToken -> memberRefreshToken.validateRefreshToken(refreshToken))
            .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token expired."));
    }


    public Jws<Claims> validateAndParseToken(String token) {	// validateTokenAndGetSubject에서 따로 분리
        return Jwts.parserBuilder()
            .setSigningKey(secretKey.getBytes())
            .build()
            .parseClaimsJws(token);
    }
    private String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
            new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
            Map.class
        ).get("sub").toString();
    }
}
