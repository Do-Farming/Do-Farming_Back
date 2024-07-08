package com.hana.api.dailyRank.repository;

import com.hana.api.dailyRank.entity.DailyRank;
import com.hana.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyRankRepository extends JpaRepository<DailyRank, Long> {

    @Query("SELECT dr FROM daily_rank dr WHERE dr.user.userCode = :userCode ORDER BY dr.dailyDate DESC")
    Optional<DailyRank> findLatestByUser(@Param("userCode") UUID userCode);

    List<DailyRank> findByGroupIdAndDailyDate(long group_id, LocalDate dailyDate);

    @Query("SELECT dr FROM daily_rank dr WHERE dr.user.userCode = :userCode ORDER BY dr.dailyDate DESC")
    List<DailyRank> findTop1ByUserOrderByDailyDateDesc(@Param("userCode") UUID userCode);

    @Query("SELECT dr FROM daily_rank dr WHERE dr.group.id = :groupId AND dr.dailyDate = :dailyDate AND dr.user = :user")
    Optional<DailyRank> findByGroupIdAndDailyDateAndUser(@Param("groupId") Long groupId, @Param("dailyDate") LocalDate dailyDate, @Param("user") User user);
}
