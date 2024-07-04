package com.hana.api.dailyRank.repository;

import com.hana.api.dailyRank.entity.DailyRank;
import com.hana.api.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DailyRankRepository extends JpaRepository<DailyRank, Long> {
    }
