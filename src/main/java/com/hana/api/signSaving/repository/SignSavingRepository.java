package com.hana.api.signSaving.repository;

import com.hana.api.signSaving.entity.SignSaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SignSavingRepository extends JpaRepository<SignSaving, Long> {
    @Query("select s from sign_saving s join fetch s.account join fetch s.depositsProduct where s.account.id = :accountId ")
    SignSaving findByAccountId(@Param(value = "accountId") Long accountId);
}
