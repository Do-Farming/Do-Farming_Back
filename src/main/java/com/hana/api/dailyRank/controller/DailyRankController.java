package com.hana.api.dailyRank.controller;

import com.hana.api.auth.Auth;
import com.hana.api.dailyRank.dto.DailyRankHistoryResponseDto;
import com.hana.api.dailyRank.dto.Response.DailyRankResponse;
import com.hana.api.dailyRank.entity.DailyRankHistory;
import com.hana.api.dailyRank.service.DailyRankService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    @Operation(summary = "그룹 랭킹 히스토리 조회")
    @GetMapping("/group/history")
    public BaseResponse.SuccessResult<List<DailyRankHistoryResponseDto>> getGroupDailyRankHistory(@RequestParam Long groupId, @Parameter(hidden = true) @Auth String userCode) {
        UUID userId = UUID.fromString(userCode);
        List<DailyRankHistoryResponseDto> response = dailyRankService.getDailyRankHistory(groupId, userId);
        log.info("Fetched Daily Rank History: {}", response);
        return BaseResponse.success(response);
    }
}
