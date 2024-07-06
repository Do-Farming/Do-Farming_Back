package com.hana.api.card.repository;

import com.hana.api.card.entity.Card;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findCardByType(String type);

    @Query("SELECT c FROM card c WHERE c.benefit LIKE %:tag% AND c.type = 'CRD' ORDER BY c.ranking ASC")
    List<Card> findTopRankingCardByTag(String tag, Pageable pageable);

    default Optional<Card> findTopRankingCardByTag(String tag) {
        List<Card> result = findTopRankingCardByTag(tag, PageRequest.of(0, 1));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}