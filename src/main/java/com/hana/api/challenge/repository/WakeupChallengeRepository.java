package com.hana.api.challenge.repository;

import com.hana.api.challenge.entity.WakeupChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WakeupChallengeRepository extends JpaRepository<WakeupChallenge, Long> {
    List<WakeupChallenge> findByWakeupDateAndGroupId(LocalDate wakeupDate, Long groupId);
}
