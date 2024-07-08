package com.hana.api.challenge.controller;

import com.hana.api.auth.Auth;
import com.hana.api.challenge.dto.response.PedometerResponse;
import com.hana.api.challenge.service.QuizChallengeService;
import com.hana.api.challenge.service.WalkChallengeService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@Slf4j
public class QuizChallengeController {
    private final QuizChallengeService quizChallengeService;

    @Operation(summary = "퀴즈 점수 업로드")
    @PostMapping("/post")
    public BaseResponse.SuccessResult<String> postQuizData(@Parameter(hidden = true) @Auth String userCode, @RequestParam int score, @RequestParam long completionTime) {
        UUID uuid = UUID.fromString(userCode);
        quizChallengeService.storeQuizChallengeData(uuid, score, completionTime);
        return BaseResponse.success("퀴즈 업로드 성공");
    }
}
