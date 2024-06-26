package com.hana.api.signSaving.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SavingJoinRequestDto {

    private Long savingProductId;

    private Long withdrawAccountId;
    private Long depositAmount;
    private String accountPassword;

    private Integer contractYears;
    private Double interestRate;
    private Boolean snsNotice;
}
