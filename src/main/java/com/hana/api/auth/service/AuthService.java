package com.hana.api.auth.service;

import com.hana.api.auth.dto.CertificationDto;
import com.hana.api.auth.dto.CustomerUserInfoDto;
import com.hana.api.auth.dto.JwtToken;
import com.hana.api.auth.jwt.JwtTokenProvider;
import com.hana.api.user.entity.User;
import com.hana.api.user.repository.CustomerRepository;
import com.hana.common.config.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public User loadUserByPhoneNumber(CustomerUserInfoDto customerUserInfoDto) {
        // 해당하는 유저를 찾을 수 없는 경우
        return customerRepository.findByPhoneNumber(customerUserInfoDto.getPhoneNumber()).orElseThrow(
                () -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID)
        );
    }

    public JwtToken generateJwtToken(User customer) {
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(customer.getUserCode());
        String refreshToken = jwtTokenProvider.createRefreshToken(customer.getUserCode());

        // Redis에 Refresh Token 저장 (만료 시간 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + customer.getUserCode(), refreshToken, jwtTokenProvider.getRefreshExpireTime(),
                        TimeUnit.MILLISECONDS);
        return new JwtToken(accessToken, refreshToken);
    }

    public JwtToken reissue(String refreshToken) {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BaseException(BaseResponseStatus.EXPIRED_TOKEN);
        }

        // refreshToken에서 User ID를 가져옵니다.
        String userCode = jwtTokenProvider.getUserCodeFromToken(refreshToken);

        // Redis에서 저장된 Refresh Token 값을 가져옵니다.
        String storedRefreshToken = (String) redisTemplate.opsForValue().get("RT:" + userCode);

        // 저장된 Refresh Token이 없거나 일치하지 않는 경우
        if (ObjectUtils.isEmpty(storedRefreshToken) || !storedRefreshToken.equals(refreshToken)) {
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        // 새로운 토큰 생성
        return generateJwtToken((User) customerRepository.findByUserCode(UUID.fromString(userCode))
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID)));


    }

    public void logout(String accessToken) {
        // Access Token 검증
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new BaseException(BaseResponseStatus.SYSTEM_ERROR);
        }

        // Access Token에서 User ID를 가져옵니다.
        String userCode = jwtTokenProvider.getUserCodeFromToken(accessToken);
        log.info(userCode.toString());
        // Redis에서 해당 User ID로 저장된 Refresh Token이 있는지 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + userCode) != null) {
            redisTemplate.delete("RT:" + userCode);
        }

        // Access Token 유효 시간 가져와서 Blacklist로 저장
        Long expiration = jwtTokenProvider.getAccessExpireTime();
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public Boolean certification(CertificationDto certificationDto) {
        Object certification = redisTemplate.opsForValue().get(certificationDto.getPhoneNumber());
        if (certification == null) {
            throw new BaseException(BaseResponseStatus.REQUEST_ERROR);
        }
        String code = (String) certification;
        if (!code.equals(certificationDto.getCertificationCode())) {
            throw new BaseException(BaseResponseStatus.REQUEST_ERROR);
        }

        return Boolean.TRUE;
    }


}