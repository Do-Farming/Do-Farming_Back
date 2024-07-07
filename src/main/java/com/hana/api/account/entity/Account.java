package com.hana.api.account.entity;

import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.user.entity.User;
import com.hana.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity(name="account")
@Table(name="account")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Account extends BaseEntity {

    // 계좌 아이디
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 고객 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    // 계좌번호
    private Long accountNumber;

    // 계좌 비밀번호
    private String password;

    // 계좌명
    private String name;

    // 잔액
    @Setter
    private long balance;

}