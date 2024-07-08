package com.hana.api.quiz.repository;

import com.hana.api.quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Question, Long> {
    @Query("SELECT a.choice from answer a WHERE a.question.id = :questionId")
    List<String> findAnswerByQuestionId(Long questionId);

    @Query("SELECT COUNT(a) FROM answer a WHERE a.question.id = :questionId AND a.id <= " +
            "(SELECT ans.id FROM answer ans WHERE ans.question.id = :questionId AND ans.isCorrect = true)")
    int findCorrectAnswerIndexByQuestionId(Long questionId);
}