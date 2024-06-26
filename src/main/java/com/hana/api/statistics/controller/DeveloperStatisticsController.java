package com.hana.api.statistics.controller;

import com.hana.api.history.dto.DailySettlementDto;
import com.hana.api.history.service.HistoryService;
import com.hana.api.statistics.dto.CalculateSettlementDto;
import com.hana.api.statistics.entity.StatisticsSettlement;
import com.hana.api.statistics.service.StatisticsSettlementService;
import com.hana.common.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/developer/statistics")
@RequiredArgsConstructor
public class DeveloperStatisticsController {

    private final StatisticsSettlementService statisticsSettlementService;
    private final HistoryService historyService;

    @Operation(summary = "[🧑🏻‍💻developer] 특정 날짜의 입출금 정산 계산")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "정산 및 데이터 업데이트 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class)))
    })
    @PostMapping("/calculate")
    public BaseResponse.SuccessResult<StatisticsSettlement> getHistoryByAccountId(@RequestBody CalculateSettlementDto request) {
        LocalDate dealDate = request.getDealDate();

        // 계산
        List<DailySettlementDto> dailySettlementDtoList = historyService.getStatistics(dealDate);

        if(statisticsSettlementService.existStatisticsSettlement(dealDate)){
            // 업데이트
            statisticsSettlementService.updateByDate(dealDate, dailySettlementDtoList);
        } else {
            // 저장
            statisticsSettlementService.save(dealDate, dailySettlementDtoList);
        }

        // 결과 반환
        StatisticsSettlement response = statisticsSettlementService.getByDate(dealDate);
        return BaseResponse.success(response);
    };
}
