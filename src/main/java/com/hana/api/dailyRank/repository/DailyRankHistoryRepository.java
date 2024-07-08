package com.hana.api.dailyRank.repository;

import com.hana.api.dailyRank.entity.DailyRankHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DailyRankHistoryRepository extends JpaRepository<DailyRankHistory, Long> {

    @Query("SELECT drh FROM DailyRankHistory drh WHERE drh.group.id = :groupId AND drh.userCode.userCode = :userCode")
    List<DailyRankHistory> findDailyRankHistoryByGroupIdAndUserCode(Long groupId, UUID userCode);
}
