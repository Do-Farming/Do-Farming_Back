package com.hana.api.statistics.repository;

import com.hana.api.statistics.entity.StatisticsSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatisticsSettlementRepository extends JpaRepository<StatisticsSettlement, Long> {
    List<StatisticsSettlement> findByDate(LocalDate date);

    List<StatisticsSettlement> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("UPDATE statistics_settlement ss " +
            "SET ss.depositAmount=:depositAmount, " +
            "ss.depositCount=:depositCount, " +
            "ss.withdrawalAmount=:withdrawalAmount, " +
            "ss.withdrawalCount=:withdrawalCount " +
            "WHERE ss.date=:date")
    void updateByDate(
            @Param(value = "depositAmount") Long depositAmount,
            @Param(value = "depositCount") Long depositCount,
            @Param(value = "withdrawalAmount") Long withdrawalAmount,
            @Param(value = "withdrawalCount") Long withdrawalCount,
            @Param(value = "date") LocalDate date
    );
}
