package com.hana.api.dailyRank.controller;

import com.hana.api.dailyRank.dto.Response.DailyRankResponse;
import com.hana.api.dailyRank.service.DailyRankService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/daily-rank")
@Slf4j
public class DailyRankController {

    private final DailyRankService dailyRankService;

    @Operation(summary = "그룹 일일 랭킹 조회")
    @GetMapping("/group")
    public BaseResponse.SuccessResult<DailyRankResponse> getGroupDailyRanks(@RequestParam Long groupId) {
        DailyRankResponse response = dailyRankService.getDailyRanks(groupId);
        return BaseResponse.success(response);
    }
}
