package com.hana.api.challenge.repository;

import com.hana.api.challenge.entity.ChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
    ChallengeRecord findByChallengeDate(LocalDate challengeDate);
}

