package com.hana.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.apache.log4j.spi.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Component
public class Response {

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Body {

        private int state;
        private String result;
        private ErrorCode errorCode;
        private Object data;
        private Object error;
    }

    public ResponseEntity<?> success(Object data, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("success")
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * <p> 메세지만 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : message,
     *         "data" : [],
     *         "error" : []
     *     }
     * </pre>
     *
     * @return 응답 객체
     */
    public ResponseEntity<?> success() {
        return success(Collections.emptyList(), HttpStatus.OK);
    }

    /**
     * <p> 데이터만 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : null,
     *         "data" : [{data1}, {data2}...],
     *         "error" : []
     *     }
     * </pre>
     *
     * @param data 응답 바디 data 필드에 포함될 정보
     * @return 응답 객체
     */
    public ResponseEntity<?> success(Object data) {
        return success(data, HttpStatus.OK);
    }


    public ResponseEntity<?> fail(Object data, ErrorCode errorCode, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("fail")
                .errorCode(errorCode)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * <p> 메세지를 가진 실패 응답을 반환한다. </p>
     * <pre>
     *     {
     *         "state" : HttpStatus Code,
     *         "result" : fail,
     *         "message" : message,
     *         "data" : [],
     *         "error" : [{error1}, {error2}...]
     *     }
     * </pre>
     *
     * @param status 응답 바디 status 필드에 포함될 응답 상태 코드
     * @return 응답 객체
     */
    public ResponseEntity<?> fail(ErrorCode errorCode, HttpStatus status) {
        return fail(Collections.emptyList(), errorCode, status);
    }

    public ResponseEntity<?> invalidFields(LinkedList<LinkedHashMap<String, String>> errors) {
        Body body = Body.builder()
                .state(HttpStatus.BAD_REQUEST.value())
                .data(Collections.emptyList())
                .result("fail")
                .error(errors)
                .build();
        return ResponseEntity.ok(body);
    }
}