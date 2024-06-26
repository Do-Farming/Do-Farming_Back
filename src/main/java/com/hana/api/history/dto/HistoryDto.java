package com.hana.api.history.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoryDto {
    private Long id;
    private Long accountNumber;
    private LocalDateTime dealdate;
    private String dealClassification;
    private Long remainBalance;
    private Long amount;
    private String recipient;
    private String recipientBank;
    private Long recipientNumber;
    private String sender;
    private String recipientRemarks;
    private String senderRemarks;
}