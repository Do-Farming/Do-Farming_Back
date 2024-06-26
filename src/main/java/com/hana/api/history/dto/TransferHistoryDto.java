package com.hana.api.history.dto;
import com.hana.api.account.entity.Account;
import com.hana.api.history.entity.History;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@Setter
public class TransferHistoryDto {

    @NotEmpty
    private String dealClassification;

    private Long amount;

    private Long remainBalance;

    private String recipient;
    @NotEmpty
    private String recipientBank;

    private Long recipientNumber;

    private Long senderNumber;

    private String sender;
    @NotEmpty
    private String recipientRemarks;
    @NotEmpty
    private String senderRemarks;
    @NotEmpty
    private Account account;

    public History toEntity() {
        return History.builder()
                .dealClassification(dealClassification)
                .amount(amount)
                .recipient(recipient)
                .recipientBank(recipientBank)
                .recipientNumber(recipientNumber)
                .sender(sender)
                .recipientRemarks(recipientRemarks)
                .senderRemarks(senderRemarks)
                .account(account)
                .balance(remainBalance)
                .build();
    }

}
