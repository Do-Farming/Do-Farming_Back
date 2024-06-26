package com.hana.api.statistics.controller;

import com.hana.api.statistics.dto.GetWeeklySettlementDto;
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
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsSettlementService statisticsSettlementService;

    @Operation(summary = "[😈Admin] 최근 일주일 정산 집계 데이터 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "정산 데이터 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class)))
    })
    @GetMapping("/settlement")
    public BaseResponse.SuccessResult<GetWeeklySettlementDto> getWeeklySettlement() {
        LocalDate startDate = LocalDate.now().minusDays(8);
        LocalDate endDate = LocalDate.now().minusDays(1);
        List<StatisticsSettlement> settlements = statisticsSettlementService.getAllByDateBetween(startDate, endDate);

        GetWeeklySettlementDto weeklySettlementDto = new GetWeeklySettlementDto(startDate, endDate);
        for(StatisticsSettlement settlement : settlements) {
            weeklySettlementDto.addData(settlement);
        }
        return BaseResponse.success(weeklySettlementDto);
    }
}
