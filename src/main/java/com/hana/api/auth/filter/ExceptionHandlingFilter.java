package com.hana.api.auth.filter;
import com.hana.common.config.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
@Order(1)
public class ExceptionHandlingFilter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver; //빈으로 의존성 주입받기
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 필터 체인의 다음 필터를 호출
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            // BaseException 발생 시 커스텀 예외 처리
            resolver.resolveException(request,response,null,new BaseException(BaseResponseStatus.INVALID_JWT));
        } catch (Exception e) {
            // 기타 예외 발생 시 처리
//            handleException(response, new BaseException(BaseResponseStatus.SYSTEM_ERROR));
            resolver.resolveException(request,response,null,new BaseException(BaseResponseStatus.SYSTEM_ERROR));
        }
    }
    private void handleException(HttpServletResponse response, BaseException e) throws IOException {
        // 예외 처리 로직: 응답 상태 코드 설정 및 예외 메시지를 JSON 형식으로 반환
        response.setStatus(e.getStatus().getCode());
        response.setContentType("application/json");
        response.getWriter().write("{ \"message\": \"" + e.getMessage() + "\", \"status\": " + e.getStatus().getCode() + " }");
    }
}