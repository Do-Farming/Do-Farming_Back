package com.hana.api.challenge.wakeup.controller;


import com.hana.api.challenge.wakeup.service.WakeupChallengeService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wakeup")
@Slf4j
public class WakeupController {

    private final WakeupChallengeService wakeupChallengeService;

    @Operation(summary = "오늘 인증할 사물 조회")
    @GetMapping("/object")
    public BaseResponse.SuccessResult<String> todayObject() {
        return BaseResponse.success(wakeupChallengeService.getTodayObject());
    }
}
