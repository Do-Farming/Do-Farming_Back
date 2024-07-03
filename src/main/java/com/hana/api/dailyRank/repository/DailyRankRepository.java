package com.hana.api.dailyRank.repository;

import com.hana.api.dailyRank.entity.DailyRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyRankRepository extends JpaRepository<DailyRank, Long> {

    @Query("SELECT dr FROM daily_rank dr WHERE dr.user.userCode = :userCode ORDER BY dr.dailyDate DESC")
    Optional<DailyRank> findLatestByUser(UUID userCode);

    List<DailyRank> findByGroupIdAndDailyDate(long group_id, LocalDate dailyDate);
}
    }
