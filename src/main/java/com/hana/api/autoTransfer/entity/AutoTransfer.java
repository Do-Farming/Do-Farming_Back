package com.hana.api.autoTransfer.entity;

import com.hana.api.account.entity.Account;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity(name = "auto_transfer")
@Table(name = "auto_transfer")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class AutoTransfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "recipien_bank", length = 100, nullable = false)
    private String recipientBank;

    @Column(name = "to_account_number", length = 13, nullable = false)
    private Long toAccountNumber;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "auto_transfer_day", nullable = false)
    private int autoTransferDay;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String recipientRemarks;
    private String senderRemarks;
}