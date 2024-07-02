package com.hana.api.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CustomerUserInfoDto {
    @NotEmpty(message = "전화번호는 필수 입력값입니다.")
    private String phoneNumber;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
