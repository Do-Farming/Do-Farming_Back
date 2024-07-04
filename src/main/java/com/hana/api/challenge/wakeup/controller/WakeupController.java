package com.hana.api.challenge.wakeup.controller;


import com.hana.api.auth.Auth;
import com.hana.api.challenge.wakeup.dto.WakeupResponseDto;
import com.hana.api.challenge.wakeup.service.WakeupChallengeService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @Operation(summary = "기상 인증")
    @PostMapping("/certificate")
    public BaseResponse.SuccessResult<String> wakeupCertificate(@Parameter(hidden = true) @Auth String userCode) {
        UUID uuid = UUID.fromString(userCode);
        wakeupChallengeService.postWakeupCertificate(uuid);
        return BaseResponse.success("기상 인증 성공");
    }

    @Operation(summary = "특정 그룹의 기상 시간 리스트 조회")
    @GetMapping("/{groupId}/wakeuptimes")
    public BaseResponse.SuccessResult<List<WakeupResponseDto.WakeupCertificateDto>> getWakeupCertificate(@PathVariable long groupId) {
        return BaseResponse.success(wakeupChallengeService.getWakeupTimesByGroupId(groupId));
    }

}
