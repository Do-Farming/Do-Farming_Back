package com.hana.api.signSaving.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DofarmingJoinRequestDto {
    private Long dofarmingProductId;
    private Long withdrawAccountId;
    private Long depositAmount = 1000000L;
    private String accountPassword;
}
