package com.hana.api.account.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hana.api.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Boolean existsAccountByAccountNumber(Long accountNumber);
    @Query("SELECT a FROM account a join fetch a.user where a.user.userCode = :userCode")
    List<Account> findAccountByCustomerId(@Param(value = "userCode") UUID userCode);

    Optional<Account> findByAccountNumber(Long recipientAccountNumber);
}
