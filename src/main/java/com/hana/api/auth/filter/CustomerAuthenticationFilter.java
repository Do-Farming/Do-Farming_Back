package com.hana.api.auth.filter;


import com.hana.api.auth.jwt.JwtTokenExtractor;
import com.hana.api.auth.jwt.JwtTokenProvider;
import com.hana.common.config.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
@Slf4j
@Component
public class CustomerAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final List<String> EXCLUDE_URL = List.of("/**", "/api/v1/auth", "/api/v1/customer/signup", "/favicon",
            "/api/v1/send-notification", "/register-token","/api/send-notification", "/api/v1/account/simple",
            "/api/v1/sms", "/swagger", "/v3", "/api/v1/developer", "/api/ocr","/api/gpt/generate-image","/api/gpt/chat", "/api/v1/deposits-products/list");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("MemberAuthFilter Start : {}", request.getServletPath());

        String token = JwtTokenExtractor.extractJwt(request);
        String userCode = tokenProvider.getUserCodeFromToken(token);
        request.setAttribute("userCode", userCode);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> request.getServletPath().startsWith(exclude));
    }
}