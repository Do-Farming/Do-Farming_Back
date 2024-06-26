package com.hana.api.history.repository;

import com.hana.api.history.dto.DailySettlementDto;
import com.hana.api.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query("SELECT h FROM history h WHERE h.account.id IS NOT NULL AND h.account.id = :accountId")
    List<History> findByAccountId(@Param(value = "accountId") Long accountId);

    @Query("SELECT new com.hana.api.history.dto.DailySettlementDto(dealClassification, count(*), sum(amount) ) " +
            "FROM history " +
            "WHERE dealdate BETWEEN :startDate AND :endDate " +
            "GROUP BY dealClassification")
    List<DailySettlementDto> getStatistics(@Param(value = "startDate")LocalDateTime startDate, @Param(value = "endDate")LocalDateTime endDate);
}