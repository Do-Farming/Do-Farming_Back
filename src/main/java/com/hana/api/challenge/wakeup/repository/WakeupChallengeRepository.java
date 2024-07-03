package com.hana.api.challenge.wakeup.repository;

import com.hana.api.challenge.wakeup.entity.WakeupChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WakeupChallengeRepository extends JpaRepository<WakeupChallenge, Long> {
}
