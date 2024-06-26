package com.hana.api.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountValidationRequest {
    private Long accountId;
    private String accountPassword;

}
