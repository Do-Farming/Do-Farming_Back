package com.hana.api.auth.controller;


import com.hana.api.auth.dto.CertificationDto;
import com.hana.api.auth.dto.CustomerUserInfoDto;
import com.hana.api.auth.dto.JwtToken;
import com.hana.api.auth.jwt.JwtTokenExtractor;
import com.hana.api.auth.service.AuthService;
import com.hana.api.user.entity.User;
import com.hana.common.config.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public BaseResponse.SuccessResult<JwtToken> login(@RequestBody CustomerUserInfoDto loginRequest) {
        // 사용자 정보 로드
        User userDetails = authService.loadUserByPhoneNumber(loginRequest);

        // 비밀번호 검증
        if (!userDetails.getPassword().equals(loginRequest.getPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }
        // JWT 토큰 생성
        return BaseResponse.success(authService.generateJwtToken(userDetails));
    }

    @PostMapping("/reissue")
    public BaseResponse.SuccessResult<JwtToken> reissue(HttpServletRequest request) {
        // refresh 토큰 추출
        String refreshToken = JwtTokenExtractor.extractRefresh(request);
        return BaseResponse.success(authService.reissue(refreshToken));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String accessToken = JwtTokenExtractor.extractJwt(request);
        authService.logout(accessToken);
    }

    @PostMapping("/certification")
    public BaseResponse.SuccessResult<Boolean> certificate(@RequestBody CertificationDto certificationDto) {
        return BaseResponse.success(authService.certification(certificationDto));
    }
}
