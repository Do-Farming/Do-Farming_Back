package com.hana.api.challenge.wakeup.repository;

import com.hana.api.challenge.wakeup.entity.WakeupChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WakeupChallengeRepository extends JpaRepository<WakeupChallenge, Long> {
    List<WakeupChallenge> findByWakeupDateAndGroupId(LocalDate wakeupDate, Long groupId);
}
