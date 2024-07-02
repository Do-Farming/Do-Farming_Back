package com.hana.api.card.repository;

import com.hana.api.account.entity.Account;
import com.hana.api.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findCardByType(String type);

//    @Query("SELECT c FROM card c where c.type = :type LIMIT :count")
//    List<Card> findCardByTypeAndCount(@Param(value = "type") String type, @Param(value = "count") int count);
}