package com.hana.api.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailySettlementAmountDto {
    private String dateString;
    private Long depositAmount = 0L;
    private Long withdrawalAmount= 0L;
}