package com.hana.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // User
    USER_REGISTER_CHECK_SUCCESS(OK, "회원가입 여부 확인 성공"),

    // Group
    GROUP_CREATE_SUCCESS(CREATED,"챌린지 그룹 생성 성공");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusValue() {
        return httpStatus.value();
    }
}
