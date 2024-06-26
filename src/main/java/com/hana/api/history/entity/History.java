package com.hana.api.history.entity;


import com.hana.api.account.entity.Account;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "history")
@Table(name = "history")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class History extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 출금계좌번호(id기준 조회)
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @CreatedDate
    private LocalDateTime dealdate;

    private String dealClassification;
    //거래액
    private Long amount;
    //거래 후 잔액
    private Long balance;
    // 받는 사람 이름
    private String recipient;
    // 받는 사람 은행
    private String recipientBank;
    // 받는 사람 계좌 번호
    private Long recipientNumber;
    // 보내는 사람 계좌 번호
    private Long senderNumber;

    // 보내는 사람
    private String sender;

    // 받는 분 표기
    private String recipientRemarks;
    // 보내는 분 표기
    private String senderRemarks;
}