package com.hana.api.history.controller;


import com.hana.api.history.dto.HistoryDto;
import com.hana.api.history.service.HistoryService;
import com.hana.common.config.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@Tag(name = "History API", description = "이체기록 api")
public class HistoryController {
    private final HistoryService historyService;

    @Operation(summary = "계좌별 이체기록", description = "accountId에 따른 return")
    @GetMapping("/account/{accountId}")
    public BaseResponse.SuccessResult<List<HistoryDto>> getHistoryByAccountId(@PathVariable("accountId") Long accountId) {
        return BaseResponse.success(historyService.getHistoryByAccountId(accountId));
    };
}