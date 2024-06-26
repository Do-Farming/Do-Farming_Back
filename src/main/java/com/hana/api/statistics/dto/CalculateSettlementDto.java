package com.hana.api.statistics.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CalculateSettlementDto {
    @NotEmpty(message = "정산 데이터 계산할 날짜 필수로 입력해주세요.")
    private LocalDate dealDate;
}