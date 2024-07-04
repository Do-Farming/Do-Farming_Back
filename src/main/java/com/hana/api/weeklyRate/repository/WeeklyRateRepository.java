package com.hana.api.weeklyRate.repository;

import com.hana.api.weeklyRate.entity.WeeklyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyRateRepository extends JpaRepository<WeeklyRate, Long> {
}
