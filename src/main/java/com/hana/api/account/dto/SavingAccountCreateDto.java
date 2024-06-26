package com.hana.api.account.dto;


import com.hana.api.account.entity.Account;
import com.hana.api.user.entity.User;
import jakarta.validation.constraints.NotEmpty;

public class SavingAccountCreateDto extends AccountCreateDto {

    private final String savingAccountCode = "01";

    public SavingAccountCreateDto(@NotEmpty String accountName,
                                  @NotEmpty String accountPassword,
                                  @NotEmpty Long balance) {
        super(accountName, accountPassword, balance);
    }

    @Override
    public Account toEntity(Long accountNumber, User user) {
        return Account.builder().accountNumber(accountNumber)
                .name(super.getAccountName())
                .balance(super.getBalance())
                .password(super.getAccountPassword())
                .user(user)
                .build();
    }

    @Override
    public String getAccountCode() {
        return savingAccountCode;
    }
}
