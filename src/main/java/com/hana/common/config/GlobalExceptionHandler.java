package com.hana.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public BaseResponse.ErrorResult handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return BaseResponse.fail(BaseResponseStatus.SYSTEM_ERROR);
    }
    @ExceptionHandler({BaseException.class})
    public BaseResponse.ErrorResult handleBaseException(BaseException e) {
        return BaseResponse.fail(e.getStatus());
    }

}
