package com.hana.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CustomerContactDto {
    private UUID userCode;
    private String name;
    private String phoneNumber;
}
