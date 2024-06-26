package com.hana.api.account.dto;


import com.hana.api.account.entity.Account;
import com.hana.api.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
@NoArgsConstructor
@ToString
public class NormalAccountCreateDto extends AccountCreateDto {

    private final String normalAccountCode = "03";

    public NormalAccountCreateDto(@NotEmpty String accountName,
                                  @NotEmpty String accountPassword,
                                  @NotEmpty Long balance) {
        super(accountName, accountPassword, balance);
    }

    @Override
    public Account toEntity(Long accountNumber, User customer) {
        log.info(String.valueOf(super.getBalance()));
        return Account.builder().accountNumber(accountNumber)
                .name(super.getAccountName())
                .balance(super.getBalance())
                .password(super.getAccountPassword())
                .user(customer)
                .build();
    }

    @Override
    public String getAccountCode() {
        return normalAccountCode;
    }
}
