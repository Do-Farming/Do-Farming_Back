package com.hana.api.auth.jwt;

import com.hana.common.config.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessExpireTime = 30 * 60 * 1000L;
    private final long refreshExpireTime = 24 * 60 * 60 * 1000L;
    private final JwtParser jwtParser;
    private final String ISSUE;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.issuer}") String issue) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.ISSUE = issue;
    }

    public String createAccessToken(UUID userCode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userCode", userCode);
        return createToken(accessExpireTime, payload);
    }

    public String createRefreshToken(UUID userCode)  {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userCode", userCode);
        return createToken(refreshExpireTime, payload);
    }

    public String createToken(long expireTime, Map<String, Object> payload ) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuer(ISSUE)
                .setClaims(payload)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(key)
                .compact();
    }


    public String getUserCodeFromToken(String token) {
        Claims claims = getValidClaim(token);
        log.info( (String) claims.get("userCode"));
        return (String) claims.get("userCode");
    }

    private Claims getValidClaim(String token) {
        try {
            log.info("Validating token {}", token);
            return parseClaim(token);
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseResponseStatus.EXPIRED_TOKEN);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new BaseException(BaseResponseStatus.INVALID_JWT);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.SYSTEM_ERROR);
        }
    }

    private Claims parseClaim(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}