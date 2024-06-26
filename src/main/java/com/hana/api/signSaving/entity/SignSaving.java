package com.hana.api.signSaving.entity;

import com.hana.api.account.entity.Account;
import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;

import lombok.*;

@Entity(name = "sign_saving")
@Table(name = "sign_saving")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class SignSaving extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "deposits_product_id")
    private DepositsProduct depositsProduct;

    private Integer contractYears; // 계약 햇수
    private Boolean snsNotice; // SNS 만기 알림
    private Double interestRate; // 금리
}