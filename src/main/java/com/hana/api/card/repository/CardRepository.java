package com.hana.api.card.repository;

import com.hana.api.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findCardByType(String type);

    @Query("SELECT c FROM card c WHERE c.benefit LIKE :tag AND c.type = 'CRD' ORDER BY c.ranking ASC LIMIT 1")
    List<Card> findTopRankingCardByTag(String tag);
}