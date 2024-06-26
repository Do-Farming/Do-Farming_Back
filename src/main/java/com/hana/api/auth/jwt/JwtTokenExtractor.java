package com.hana.api.auth.jwt;

import com.hana.common.config.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

public class JwtTokenExtractor {
    private static final String SCHEMA = "Bearer ";
    private static final String TOKEN_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Authorization-Refresh";
    private JwtTokenExtractor() {
    }

    public static String extractJwt(HttpServletRequest req) {
        String token = req.getHeader(TOKEN_HEADER);

        if (token != null && token.startsWith(SCHEMA)) {
            return token.replace(SCHEMA,"");
        }
        throw new BaseException(BaseResponseStatus.INVALID_JWT);
    }
    public static String extractRefresh(HttpServletRequest req) {
        String token = req.getHeader(REFRESH_HEADER);

        if (token != null && token.startsWith(SCHEMA)) {
            return token.replace(SCHEMA,"");
        }
        throw new BaseException(BaseResponseStatus.INVALID_JWT);
    }
}