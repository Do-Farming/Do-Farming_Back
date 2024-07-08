package com.hana.api.quiz.dto;

import com.hana.api.quiz.entity.Answer;
import com.hana.api.quiz.entity.Question;
import lombok.*;

import java.util.List;

public class QuizResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class GetQuiz {
        private String question;
        private List<String> choices;
        private int correctAnswer;
    }

}