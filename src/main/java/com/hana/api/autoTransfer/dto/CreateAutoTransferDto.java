package com.hana.api.autoTransfer.dto;

import com.hana.api.account.entity.Account;
import com.hana.api.autoTransfer.entity.AutoTransfer;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class CreateAutoTransferDto {

    private String recipientBank;
    private Long toAccountNumber;
    private Long amount;
    private int autoTransferDay;
    private LocalDate endDate;
    private LocalDate startDate;
    private Long accountId;
    private String password;
    private String recipientRemarks;
    private String senderRemarks;


    public CreateAutoTransferDto(AutoTransfer autoTransfer) {
        this.autoTransferDay = autoTransfer.getAutoTransferDay();
        this.accountId = autoTransfer.getAccount().getId();
        this.startDate = autoTransfer.getStartDate();
        this.endDate = autoTransfer.getEndDate();
        this.recipientRemarks = autoTransfer.getRecipientRemarks();
        this.senderRemarks = autoTransfer.getSenderRemarks();
        this.toAccountNumber = autoTransfer.getToAccountNumber();
        this.recipientBank = autoTransfer.getRecipientBank();
        this.password = autoTransfer.getAccount().getPassword();
        this.amount = autoTransfer.getAmount();
    }

    public AutoTransfer toEntity(Account account) {
        return AutoTransfer.builder()
                .account(account)
                .autoTransferDay(autoTransferDay)
                .amount(amount)
                .recipientBank(recipientBank)
                .recipientRemarks(recipientRemarks)
                .senderRemarks(senderRemarks)
                .startDate(startDate)
                .endDate(endDate)
                .toAccountNumber(toAccountNumber)
                .build();

    }

}