package com.hana.api.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CertificationDto {
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String certificationCode;
}
