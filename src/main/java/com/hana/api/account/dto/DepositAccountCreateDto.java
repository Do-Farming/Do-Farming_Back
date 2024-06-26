package com.hana.api.account.dto;

import com.hana.api.account.entity.Account;
import com.hana.api.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DepositAccountCreateDto extends AccountCreateDto {

    private final String DepositAccountCode = "02";

    public DepositAccountCreateDto(@NotEmpty String accountName,
                                   @NotEmpty String accountPassword,
                                   @NotEmpty Long balance) {
        super(accountName, accountPassword, balance);
    }

    @Override
    public Account toEntity(Long accountNumber, User customer) {
        return Account.builder().accountNumber(accountNumber)
                .name(super.getAccountName())
                .balance(super.getBalance())
                .password(super.getAccountPassword())
                .user(customer)
                .build();
    }

    @Override
    public String getAccountCode() {
        return DepositAccountCode;
    }
}
