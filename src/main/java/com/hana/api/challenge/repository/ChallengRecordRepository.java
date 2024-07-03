package com.hana.api.challenge.repository;

import com.hana.api.challenge.entity.ChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengRecordRepository extends JpaRepository<ChallengeRecord, Long> {
}
