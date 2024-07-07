package com.hana.api.signSaving.controller;

import com.hana.api.auth.Auth;
import com.hana.api.auth.dto.JwtToken;
import com.hana.api.auth.jwt.JwtTokenProvider;
import com.hana.api.signSaving.dto.request.DofarmingJoinRequestDto;
import com.hana.api.signSaving.dto.request.SavingJoinRequestDto;
import com.hana.api.signSaving.dto.response.AccountInfoResponse;
import com.hana.api.signSaving.dto.response.SavingJoinResponseDto;
import com.hana.api.signSaving.service.SignSavingService;
import com.hana.common.config.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/signsaving")
public class SignSavingController {
    private final SignSavingService signSavingService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "예금 상품 가입")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "상품 가입 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class)))
    })
    @PostMapping("/create")
    public BaseResponse.SuccessResult<SavingJoinResponseDto> createSavingAccount(@Auth String userCode,
                                                                                 @RequestBody SavingJoinRequestDto request) {

        UUID uuid = UUID.fromString(userCode);
        return BaseResponse.success(signSavingService.joinSavingAccount(uuid, request));
    }

    @Operation(summary = "두파밍 상품 가입")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "두파밍 상품 가입 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class)))
    })
    @PostMapping("/dofarming/create")
    public BaseResponse.SuccessResult<String> createDofarmingAccount(@Auth String userCode,
                                                                                 @RequestBody DofarmingJoinRequestDto request) {

        UUID uuid = UUID.fromString(userCode);
        signSavingService.joinDofarmingAccount(uuid, request);
        return BaseResponse.success("두파밍 상품 가입 성공");
    }

    @Operation(summary = "예금 계좌 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "조회 완료", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
            @ApiResponse(responseCode = "3200", description = "계좌 아이디 없음", content = @Content(schema = @Schema(implementation = BaseResponse.ErrorResult.class))),
    })
    @GetMapping("/{accountId}")
    public BaseResponse.SuccessResult<AccountInfoResponse> getAccountInfo(@PathVariable Long accountId) {
        try {
            AccountInfoResponse response = signSavingService.getAccountInfo(accountId);
            return BaseResponse.success(response);
        } catch (NullPointerException e) {
            throw new BaseException(BaseResponseStatus.ACCOUNTS_EMPTY_ACCOUNT_ID);
        }
    }

}
