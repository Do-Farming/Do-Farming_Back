package com.hana.api.quiz.service;

import com.hana.api.quiz.dto.QuizRequestDto;
import com.hana.api.quiz.dto.QuizResponseDto;
import com.hana.api.quiz.entity.Answer;
import com.hana.api.quiz.entity.Question;
import com.hana.api.quiz.repository.QuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final QuizRepository quizRepository;

    public List<Question> getRandomQuestions(int count) {
        // 전체 질문 목록 가져오기
        List<Question> questionList = quizRepository.findAll();

        // questionList가 empty인 경우 예외처리
        if (questionList.isEmpty()) {
            throw new RuntimeException("질문 목록이 비어 있습니다.");
        }

        // questionList에서 count 개수만큼 랜덤으로 선택
        Collections.shuffle(questionList); // 리스트를 섞음
        return questionList.subList(0, Math.min(count, questionList.size()));
    }

    public List<String> getOptionsByQuestionId(Long questionId) {
        return quizRepository.findAnswerByQuestionId(questionId);
    }

    public int getCorrectAnswerByQuestionId(Long questionId) {
        return quizRepository.findCorrectAnswerIndexByQuestionId(questionId);
    }

    public List<QuizResponseDto.GetQuiz> getQuizList(int count) {
        List<QuizResponseDto.GetQuiz> quizList = new ArrayList<>();
        List<Question> questionList = getRandomQuestions(count);

        for(Question question : questionList) {
            QuizResponseDto.GetQuiz quiz = QuizResponseDto.GetQuiz.builder()
                    .question(question.getQuestion())
                    .choices(getOptionsByQuestionId(question.getId()))
                    .correctAnswer(getCorrectAnswerByQuestionId(question.getId()))
                    .build();
            quizList.add(quiz);
        }

        return quizList;
    }
}
