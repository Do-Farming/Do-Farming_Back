package com.hana.api.quiz.controller;

import com.hana.api.quiz.dto.QuizResponseDto;
import com.hana.api.quiz.service.QuizService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizChallengeService;

    @GetMapping("")
    @Operation(summary = "Quiz List 조회")
    public BaseResponse.SuccessResult<List<QuizResponseDto.GetQuiz>> getQuizList(@RequestParam int count) {
        return BaseResponse.success(quizChallengeService.getQuizList(count));
    }
}
