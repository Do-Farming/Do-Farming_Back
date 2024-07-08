package com.hana.api.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizResponseDto {
    private int quizScore;
    private double quizTime;
}
