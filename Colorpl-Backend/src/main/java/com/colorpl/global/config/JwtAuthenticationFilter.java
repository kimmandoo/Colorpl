package com.colorpl.global.config;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(0)
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            User user = parseUserSpecification(accessToken);
            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, accessToken, user.getAuthorities());
            authenticated.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        } catch (ExpiredJwtException e) {
            String newAccessToken = reissueAccessToken(request, response, e);
            response.setHeader("New-Access-Token", newAccessToken);
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"newAccessToken\": \"" + newAccessToken + "\"}");
            response.getWriter().flush();
            return;
        } catch (Exception e) {
            request.setAttribute("exception", e);
            authenticationEntryPoint.commence(request, response, null);
            return;
        }

        filterChain.doFilter(request, response);
    }


    private String parseBearerToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
            .filter(token -> token.length() > 7 && token.substring(0, 7).equalsIgnoreCase("Bearer "))
            .map(token -> token.substring(7))
            .orElse(null);
    }

    private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(tokenProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");

        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
    }

    private String reissueAccessToken(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        try {
            String refreshToken = parseBearerToken(request, "Refresh-Token");
            if (refreshToken == null) {
                throw exception;
            }
            String oldAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);
            tokenProvider.validateRefreshToken(refreshToken, oldAccessToken);
            String newAccessToken = tokenProvider.recreateAccessToken(oldAccessToken);
            User user = parseUserSpecification(newAccessToken);
            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, newAccessToken, user.getAuthorities());
            authenticated.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            response.setHeader("New-Access-Token", newAccessToken);
            return newAccessToken;
        } catch (Exception e) {
            request.setAttribute("exception", e);
            throw new IOException("Failed to reissue access token", e);
        }
    }
}
