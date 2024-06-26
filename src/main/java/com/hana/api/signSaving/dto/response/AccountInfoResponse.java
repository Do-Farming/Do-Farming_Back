package com.hana.api.signSaving.dto.response;

import com.hana.api.signSaving.entity.SignSaving;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountInfoResponse {

    private Long accountNumber; // 계좌번호

    private String finPrditNm; //상품명

    private String type; //과목명

    private long balance; //잔액

    private LocalDateTime creatdAt; //계약일시

    private Integer contractYears; //계약햇수

    private Double interestRate; // 금리

    public static AccountInfoResponse todto(SignSaving signSaving) {
        return AccountInfoResponse.builder()
                .accountNumber(signSaving.getAccount().getAccountNumber())
                .finPrditNm(signSaving.getDepositsProduct().getFinPrdtNm())
                .type(String.valueOf(signSaving.getDepositsProduct().getType()))
                .balance(signSaving.getAccount().getBalance())
                .creatdAt(signSaving.getCreatedDate())
                .contractYears(signSaving.getContractYears())
                .interestRate(signSaving.getInterestRate())
                .build();
    }
}
