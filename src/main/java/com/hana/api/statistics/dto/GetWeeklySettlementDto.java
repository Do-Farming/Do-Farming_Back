package com.hana.api.statistics.dto;

import com.hana.api.statistics.entity.StatisticsSettlement;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetWeeklySettlementDto {
    private List<DailySettlementAmountDto> amountDataList = new ArrayList<>();

    private Long depositTotalAmount = 0L;
    private Long withdrawalTotalAmount= 0L;

    private Long depositTotalCount= 0L;
    private Long withdrawalTotalCount= 0L;

    private LocalDate startDate;
    private LocalDate endDate;

    public GetWeeklySettlementDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addData(StatisticsSettlement statisticsSettlement){
        depositTotalAmount += statisticsSettlement.getDepositAmount();
        withdrawalTotalAmount += statisticsSettlement.getWithdrawalAmount();

        depositTotalCount += statisticsSettlement.getDepositCount();
        withdrawalTotalCount += statisticsSettlement.getWithdrawalCount();

        amountDataList.add(new DailySettlementAmountDto(
                statisticsSettlement.getDate().toString(),
                statisticsSettlement.getDepositAmount(),
                statisticsSettlement.getWithdrawalAmount()
        ));
    }
}