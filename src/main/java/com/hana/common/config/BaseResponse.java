package com.hana.common.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import static com.hana.common.config.BaseResponseStatus.SUCCESS;

@Getter
public class BaseResponse {//BaseResponse 객체를 사용할때 성공, 실패 경우

    public static <T> SuccessResult<T> success(T result) {
        return new SuccessResult<>(result);
    }

    public static ErrorResult fail(BaseResponseStatus status) {
        return new ErrorResult(status);
    }

    @Getter
    public static class SuccessResult<T> {
        @JsonProperty("isSuccess")
        @Schema(description = "응답 유무", example = "true")
        private final Boolean isSuccess;
        @Schema(description = "응답 메세지", example = "요청에 성공하였습니다.")
        private final String message;
        @Schema(description = "응답 코드", example = "1000")
        private final int code;
        @Schema(description = "응답 데이터")
        private final T result;

        private SuccessResult(T result) {
            this.isSuccess = SUCCESS.isSuccess();
            this.message = SUCCESS.getMessage();
            this.code = SUCCESS.getCode();
            this.result = result;
        }

    }

    @Getter
    @JsonPropertyOrder({"isSuccess", "code", "message", "result"})
    public static class ErrorResult {
        @JsonProperty("isSuccess")
        @Schema(description = "응답 유무", example = "false")
        private final Boolean isSuccess;
        @Schema(description = "응답 메세지", example = "알 수 없는 오류 서버팀에 문의주세요.")
        private final String message;
        @Schema(description = "커스텀 응답 코드", example = "5000")
        private final int code;

        private ErrorResult(BaseResponseStatus baseResponseStatus) {
            this.isSuccess = baseResponseStatus.isSuccess();
            this.message = baseResponseStatus.getMessage();
            this.code = baseResponseStatus.getCode();
        }
    }

}

