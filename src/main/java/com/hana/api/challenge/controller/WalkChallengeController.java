package com.hana.api.challenge.controller;

import com.hana.api.auth.Auth;
import com.hana.api.challenge.dto.response.PedometerResponse;
import com.hana.api.challenge.service.WalkChallengeService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedometer")
@RequiredArgsConstructor
@Slf4j
public class WalkChallengeController {

    private final WalkChallengeService walkChallengeService;

    @GetMapping("/get")
    public BaseResponse.SuccessResult<List<PedometerResponse>> getGroupPedometer(@RequestParam Long groupId) {
        List<PedometerResponse> response = walkChallengeService.getGroupPedometer(groupId);
        return BaseResponse.success(response);
    }

    @PostMapping("/post")
    public BaseResponse.SuccessResult<String> postPedometer(@Parameter(hidden = true) @Auth String userCode, @RequestParam int step) {
        UUID uuid = UUID.fromString(userCode);
        log.info(String.valueOf(step));
        walkChallengeService.saveWalkChallenge(uuid, step);
        return BaseResponse.success("내 걸음 수 업로드 성공");
    }
}
