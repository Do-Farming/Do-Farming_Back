package com.hana.api.autoTransfer.controller;

import com.hana.api.autoTransfer.dto.CreateAutoTransferDto;
import com.hana.api.autoTransfer.service.AutoTransferService;
import com.hana.common.config.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/autoTransfer")
public class AutoTransferController {

    private final AutoTransferService autoTransferService;

    @Operation(summary = "자동이체 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "자동이체 등록 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
            @ApiResponse(responseCode = "3500", description = "자동이체 등록 실패", content = @Content(schema = @Schema(implementation = BaseResponse.ErrorResult.class))),
    })
    @PostMapping("/register")
    public BaseResponse.SuccessResult<Long> getAccount(@RequestBody CreateAutoTransferDto createAutoTransferDto) {
        log.info("자동이체 등록 : {}", createAutoTransferDto.toString());
        return BaseResponse.success(autoTransferService.createAutoTransfer(createAutoTransferDto));
    }

}