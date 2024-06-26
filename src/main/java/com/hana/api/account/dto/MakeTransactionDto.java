package com.hana.api.account.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MakeTransactionDto {
    private Long accountId;
    private Long amount;
    private String password;
    private String recipientBank;
    private Long recipientAccountNumber;
    private String recipientRemarks;
    private String senderRemarks;
}
