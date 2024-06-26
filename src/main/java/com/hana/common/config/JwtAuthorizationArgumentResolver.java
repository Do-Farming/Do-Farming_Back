package com.hana.common.config;

import com.hana.api.auth.Auth;
import com.hana.api.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        try {
            if (request != null) {
                String token = request.getHeader(AUTHORIZATION);
                if (token != null && token.startsWith(BEARER)) {
                    // JWT 토큰에서 유저 ID 추출
                    log.info("Argument Resolver Working");
                    return jwtTokenProvider.getUserCodeFromToken(token.substring(BEARER.length()));
                }
            }
        } catch (Exception e) {
            // 예외 발생 시 BaseException을 던짐
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }
        // 토큰이 없거나 유효하지 않은 경우 null 반환
        return null;
    }
}
