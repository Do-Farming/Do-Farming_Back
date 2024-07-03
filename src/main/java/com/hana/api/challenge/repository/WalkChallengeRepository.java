package com.hana.api.challenge.repository;

import com.hana.api.challenge.entity.WalkChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WalkChallengeRepository extends JpaRepository<WalkChallenge, Long> {
    List<WalkChallenge> findByWalkDateAndGroupId(LocalDate walkDate, Long groupId);
}
