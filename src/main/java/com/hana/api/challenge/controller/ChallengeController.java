package com.hana.api.challenge.controller;

import com.hana.api.auth.Auth;
import com.hana.api.challenge.dto.response.PedometerResponse;
import com.hana.api.challenge.service.ChallengeRecordService;
import com.hana.api.challenge.service.WalkChallengeService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
@Slf4j
public class ChallengeController {

    final ChallengeRecordService challengeRecordService;

    @GetMapping("/getChallenge")
    public BaseResponse.SuccessResult<Integer> getChallenge() {
        int response = challengeRecordService.getChallenge();
        return BaseResponse.success(response);
    }
}
