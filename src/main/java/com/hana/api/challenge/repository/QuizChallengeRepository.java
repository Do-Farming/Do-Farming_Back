package com.hana.api.challenge.repository;

import com.hana.api.challenge.entity.QuizChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface QuizChallengeRepository extends JpaRepository<QuizChallenge, Long> {
    List<QuizChallenge> findByQuizDateAndGroupId(LocalDate quizDate, Long groupId);
}
