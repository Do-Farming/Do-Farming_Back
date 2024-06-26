package com.hana.api.account.dto;

import com.hana.api.account.entity.Account;
import com.hana.api.user.entity.User;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public abstract class AccountCreateDto {
    private final String branchCode = "111";
    @NotEmpty
    private String accountName;
    @NotEmpty
    private String accountPassword;
    @NotEmpty
    private Long balance;

    public abstract Account toEntity(Long accountNumber, User customer);
    public abstract String getAccountCode();
}