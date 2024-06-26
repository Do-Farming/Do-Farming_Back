package com.hana.api.signSaving.dto.response;


import com.hana.api.signSaving.entity.SignSaving;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SavingJoinResponseDto {
    private Long id;
    private Long accountNumber;
    private String name;
    private long balance;
    private Integer contractYears; // 계약 햇수
    private Boolean snsNotice; // SNS 만기 알림
    private Double interestRate; // 금리

    public SavingJoinResponseDto(SignSaving saving) {
        this.id = saving.getId();
        this.accountNumber = saving.getAccount().getAccountNumber();
        this.name = saving.getAccount().getName();
        this.balance = saving.getAccount().getBalance();
        this.contractYears = saving.getContractYears();
        this.snsNotice = saving.getSnsNotice();
        this.interestRate = saving.getInterestRate();
    }
}
